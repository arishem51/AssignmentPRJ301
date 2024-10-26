import { CommonModule } from '@angular/common';
import { Component, effect } from '@angular/core';
import {
  FormArray,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
} from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { provideNativeDateAdapter } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatDivider } from '@angular/material/divider';
import { MatInputModule } from '@angular/material/input';
import { ProductsService } from '../../services/products.service';
import { PlanCampaign, PlanResponse, ProductResponse } from '../../types';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { Observable, map, startWith } from 'rxjs';
import { PlanService } from '../../services/plan.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';

@Component({
  selector: 'app-production-plan-detail',
  standalone: true,
  imports: [
    MatInputModule,
    MatDatepickerModule,
    ReactiveFormsModule,
    MatButtonModule,
    CommonModule,
    MatDivider,
    MatAutocompleteModule,
  ],
  providers: [provideNativeDateAdapter()],
  styles: [
    `
      .container {
        padding: 0 32px;
      }
      h4 {
        text-align: center;
        margin: 16px 0 32px 0;
      }
      .plan-item-form {
        display: flex;
        gap: 8px;
      }
      mat-form-field {
        flex: 1;
      }
      .campaign-form-container {
        width: calc(66% + 4px);
      }
      .campaign-form-container div {
        display: flex;
        gap: 8px;
      }
      mat-divider {
        margin-bottom: 8px;
      }
    `,
  ],
  template: `
    <div class="container">
      <h4>{{ pageTitle }}</h4>
      <div class="plan-form-wrapper" [formGroup]="planForm">
        <div class="plan-item-form">
          <mat-form-field>
            <mat-label>Name</mat-label>
            <input matInput formControlName="name" />
          </mat-form-field>

          <mat-form-field>
            <mat-label>Start Date</mat-label>
            <input
              matInput
              [matDatepicker]="pickerStartDate"
              formControlName="startDate"
            />
            <mat-hint>MM/DD/YYYY</mat-hint>
            <mat-datepicker-toggle
              matIconSuffix
              [for]="pickerStartDate"
            ></mat-datepicker-toggle>
            <mat-datepicker #pickerStartDate></mat-datepicker>
          </mat-form-field>

          <mat-form-field>
            <mat-label>End Date</mat-label>
            <input
              matInput
              [matDatepicker]="pickerEndDate"
              formControlName="endDate"
            />
            <mat-hint>MM/DD/YYYY</mat-hint>
            <mat-datepicker-toggle
              matIconSuffix
              [for]="pickerEndDate"
            ></mat-datepicker-toggle>
            <mat-datepicker #pickerEndDate></mat-datepicker>
          </mat-form-field>
        </div>
        <div formArrayName="campaigns">
          <div
            *ngFor="let campaign of campaigns.controls; let i = index"
            [formGroupName]="i"
            class="campaign-form-container"
          >
            <h6>Product {{ i + 1 }}</h6>

            <div>
              <mat-form-field appearance="fill">
                <mat-label>Product</mat-label>
                <input
                  type="text"
                  matInput
                  [matAutocomplete]="auto"
                  formControlName="product"
                />
                <mat-autocomplete
                  #auto="matAutocomplete"
                  [displayWith]="displayProductName"
                >
                  <mat-option
                    *ngFor="let product of filteredProducts[i] | async"
                    [value]="product"
                  >
                    {{ product.name }}
                  </mat-option>
                </mat-autocomplete>
              </mat-form-field>

              <mat-form-field appearance="fill">
                <mat-label>Quantity</mat-label>
                <input
                  matInput
                  id="quantity-{{ i }}"
                  type="number"
                  formControlName="quantity"
                />
              </mat-form-field>

              <mat-form-field appearance="fill">
                <mat-label>Estimate Effort</mat-label>
                <input
                  matInput
                  id="estimateEffort-{{ i }}"
                  type="number"
                  formControlName="estimateEffort"
                />
              </mat-form-field>

              <button
                (click)="deleteCampaignItem(i)"
                color="warn"
                mat-flat-button
              >
                Delete
              </button>
            </div>

            <mat-divider></mat-divider>
          </div>
        </div>
      </div>

      <button mat-flat-button (click)="addCampaignItem()">Add Products</button>
      <button style="margin-left: 8px" mat-flat-button (click)="createPlan()">
        Create Plan
      </button>
    </div>
  `,
})
export class ProductionPlanDetail {
  planForm = new FormGroup({
    name: new FormControl(''),
    startDate: new FormControl(),
    endDate: new FormControl(),
    campaigns: new FormArray([]),
  });
  pageTitle = '';
  products: ProductResponse[] = [];
  filteredProducts: Observable<ProductResponse[]>[] = [];

  displayProductName(product: ProductResponse) {
    return product && product.name ? product.name : '';
  }

  private _filterProducts(value: string | ProductResponse) {
    if (typeof value === 'object') {
      return this.products;
    }
    const filterValue = value.toLowerCase();
    return this.products.filter((product) =>
      product.name.toLowerCase().includes(filterValue)
    );
  }

  constructor(
    private productService: ProductsService,
    private planService: PlanService,
    private _snackBar: MatSnackBar,
    private router: Router
  ) {
    effect(() => {
      const querySignal = this.productService.getProductQuerySignal();
      const query = querySignal();
      if (query) {
        this.products = query.data?.data ?? [];
      }
    });
    this.pageTitle = this.router.url.includes('create')
      ? 'Create Plan'
      : 'Plan Detail';
  }

  get campaigns(): FormArray {
    return this.planForm.get('campaigns') as FormArray;
  }

  private setupProductFiltering(index: number) {
    const productControl = this.campaigns.at(index).get('product');
    if (productControl) {
      this.filteredProducts[index] = productControl.valueChanges.pipe(
        startWith(''),
        map((value) => this._filterProducts(value || ''))
      );
    }
  }

  addCampaignItem() {
    this.campaigns.push(
      new FormGroup({
        product: new FormControl(''),
        quantity: new FormControl(0),
        estimateEffort: new FormControl(0),
      })
    );
    this.setupProductFiltering(this.campaigns.length - 1);
  }

  deleteCampaignItem(index: number) {
    this.campaigns.removeAt(index);
  }

  createPlan() {
    const plan: Omit<PlanResponse, 'id'> = {
      name: this.planForm.get('name')?.value ?? '',
      startDate: new Date(
        this.planForm.get('startDate')?.value?.toString()
      ).toISOString(),
      endDate: new Date(
        this.planForm.get('endDate')?.value?.toString()
      ).toISOString(),
      status: 'OPEN',
      campaigns: this.campaigns.value.map(
        ({
          product,
          ...rest
        }: PlanCampaign & { product: ProductResponse }) => ({
          ...rest,
          productId: product.id,
        })
      ),
    };
    this.planService.create(plan).subscribe(
      (response) => {
        this._snackBar.open('Create plan success');
        this.router.navigate(['/production-plan']);
      },
      (error) => console.error('Error in request', error)
    );
  }
}
