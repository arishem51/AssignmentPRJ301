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
import { ActivatedRoute, Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';

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
    MatCardModule,
    MatListModule,
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
            <h6>Campaign {{ i + 1 }}</h6>

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
                *ngIf="showButtons"
              >
                Delete
              </button>
            </div>

            <div *ngIf="campaign.get('scheduleCampaigns')">
              <mat-card class="campaign-card">
                <mat-card-title>Schedule Campaigns</mat-card-title>
                <mat-card-content>
                  <mat-list>
                    <mat-list-item
                      *ngFor="
                        let schedule of campaign.get('scheduleCampaigns')?.value
                      "
                    >
                      <div>
                        <p matLine>ID: {{ schedule.id }}</p>
                        <p matLine>Quantity: {{ schedule.quantity }}</p>
                        <p matLine>Date: {{ schedule.date | date }}</p>
                        <p matLine>End Date: {{ schedule.endDate | date }}</p>
                        <div>
                          Shift:
                          <div *ngFor="let shift of schedule.shifts">
                            <p>{{ shift.id }}</p>
                          </div>
                        </div>
                      </div>
                    </mat-list-item>
                  </mat-list>
                </mat-card-content>
              </mat-card>
            </div>
          </div>
        </div>
      </div>

      <button *ngIf="showButtons" mat-flat-button (click)="addCampaignItem()">
        Add Products
      </button>
      <button
        *ngIf="showButtons"
        style="margin-left: 8px"
        mat-flat-button
        (click)="createPlan()"
      >
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
    campaigns: new FormArray<FormGroup>([]),
  });
  pageTitle = '';
  products: ProductResponse[] = [];
  filteredProducts: Observable<ProductResponse[]>[] = [];
  isPlanDetail = true;
  showButtons = true;

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
    private router: Router,
    private route: ActivatedRoute
  ) {
    effect(() => {
      const querySignal = this.productService.getProductQuerySignal();
      const query = querySignal();
      if (query) {
        this.products = query.data?.data ?? [];
      }

      const id = this.route.snapshot.paramMap.get('id');
      if (id && !Number.isNaN(id)) {
        this.planService.detail(id).subscribe(({ data }) => {
          this.planForm.disable();
          this.campaigns.value?.forEach((item: FormControl) => item.disable());

          this.showButtons = false;
          const { name, startDate, endDate, planCampaigns } = data;
          this.planForm.patchValue({ name, startDate, endDate });

          const campaignControls = planCampaigns?.map(
            (campaign) =>
              new FormGroup({
                id: new FormControl(campaign.id),
                quantity: new FormControl(campaign.quantity),
                estimateEffort: new FormControl(campaign.estimateEffort),
                product: new FormControl(campaign.product),
                scheduleCampaigns: new FormArray(
                  campaign.scheduleCampaigns.map(
                    (schedule) =>
                      new FormGroup({
                        id: new FormControl(schedule.id),
                        quantity: new FormControl(schedule.quantity),
                        date: new FormControl(schedule.date),
                        endDate: new FormControl(schedule.endDate),
                        shifts: new FormArray(
                          schedule.shifts.map(
                            (shift) =>
                              new FormGroup({
                                id: new FormControl(shift.id),
                                name: new FormControl(shift.name),
                                startTime: new FormControl(shift.startTime),
                                endTime: new FormControl(shift.endTime),
                              })
                          )
                        ),
                      })
                  )
                ),
              })
          );

          if (campaignControls) {
            this.planForm.setControl(
              'campaigns',
              new FormArray(campaignControls)
            );
          }
        });
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
      planCampaigns: this.campaigns.value.map(
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
