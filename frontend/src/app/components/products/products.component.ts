import { Component, OnInit, effect, inject } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ProductsService } from '../../services/products.service';
import { PaginateMetaResponse, ProductResponse } from '../../types';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog } from '@angular/material/dialog';
import { MutateProductDialog } from './MutateProductDialog.component';
import { DeleteProductDialog } from './DeleteProductDialog.component';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';

@Component({
  standalone: true,
  selector: `app-products`,
  imports: [
    MatTableModule,
    MatProgressSpinnerModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    FormsModule,
    MatButtonModule,
    MatIconModule,
    MatPaginatorModule,
  ],
  styles: [
    `
      .container {
        padding: 24px;
      }
      .mat-elevation-z8 {
        margin-top: 20px;
        border-radius: 8px;
        overflow: hidden;
      }
      th.mat-header-cell {
        background-color: #3f51b5;
        color: white;
        font-weight: bold;
        text-align: left;
        padding: 16px;
        font-size: 14px;
        text-transform: uppercase;
        border-bottom: 2px solid #e0e0e0;
      }

      td.mat-cell {
        padding: 16px;
        font-size: 14px;
        border-bottom: 1px solid #e0e0e0;
      }
      tr.mat-row:hover {
        background-color: rgba(63, 81, 181, 0.08);
      }
      tr.mat-row:nth-child(even) td {
        background-color: #f5f5f5;
      }
      tr.mat-header-row {
        height: 56px;
      }
      tr.mat-row {
        height: 48px;
      }
      table {
        border-radius: 8px;
        overflow: hidden;
        box-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);
        width: 100%;
        border-collapse: collapse;
      }
      td.mat-cell,
      th.mat-header-cell {
        padding: 12px 16px;
      }

      /* Optional: Change row color on selection */
      tr.mat-row.mat-row-selected {
        background-color: rgba(63, 81, 181, 0.1);
      }
      td,
      th {
        text-align: center;
        vertical-align: middle;
      }
      td {
        padding: 12px 0;
      }
      .loading-container {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100%;
      }
      .search-container {
        display: flex;
        gap: 16px;
        align-items: center;
      }
      .search-container div {
        margin-left: auto;
      }
    `,
  ],
  template: `
    @if(loading){
    <div class="loading-container">
      <mat-spinner [diameter]="40"></mat-spinner>
    </div>
    }@else{
    <div class="container">
      <h5 style="text-align:center">Products</h5>
      <div class="search-container">
        <button mat-flat-button (click)="openMutateProduct()">Add</button>
        <mat-form-field class="example-form-field">
          <mat-label>Search</mat-label>
          <input
            matInput
            type="text"
            [value]="searchProduct"
            (input)="onSearchInput($event)"
          />
        </mat-form-field>
        <div>
          <mat-paginator
            [length]="meta?.totalElements"
            [pageSize]="meta?.pageSize ?? 0"
            [pageSizeOptions]="[meta?.pageSize ?? 0]"
            (page)="handlePageEvent($event)"
            aria-label="Select page"
          >
          </mat-paginator>
        </div>
      </div>

      <table mat-table [dataSource]="products" class="mat-elevation-z8">
        <ng-container matColumnDef="id">
          <th mat-header-cell *matHeaderCellDef>No.</th>
          <td mat-cell *matCellDef="let element">{{ element.id }}</td>
        </ng-container>

        <ng-container matColumnDef="name">
          <th mat-header-cell *matHeaderCellDef>Name</th>
          <td mat-cell *matCellDef="let element">{{ element.name }}</td>
        </ng-container>

        <ng-container matColumnDef="estimatedEffort">
          <th mat-header-cell *matHeaderCellDef>Effort (per hour)</th>
          <td mat-cell *matCellDef="let element">
            {{ element.estimatedEffort }}
          </td>
        </ng-container>

        <ng-container matColumnDef="img">
          <th mat-header-cell *matHeaderCellDef>Image</th>
          <td mat-cell *matCellDef="let element">
            <img [src]="element.img" alt="Image" style="width: 100px;" />
          </td>
        </ng-container>

        <ng-container matColumnDef="actions">
          <th mat-header-cell *matHeaderCellDef>Actions</th>
          <td mat-cell *matCellDef="let element">
            <div style="display:flex; gap:8px;justify-content:center">
              <button (click)="onUpdateProduct(element)" mat-flat-button>
                Update
              </button>
              <button
                color="warn"
                mat-raised-button
                (click)="openDeleteProduct(element.id)"
              >
                Delete
              </button>
            </div>
          </td>
        </ng-container>

        <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
        <tr mat-row *matRowDef="let row; columns: displayedColumns"></tr>
      </table>
    </div>
    }
  `,
})
export class Products {
  private productsSignal = inject(ProductsService).getProductQuerySignal();
  private productsMetaSignal =
    inject(ProductsService).getProductQueryMetaSignal();
  searchProduct = this.productsMetaSignal().search;

  products: ProductResponse[] = [];
  loading = false;
  meta: PaginateMetaResponse | null = null;

  handlePageEvent(e: PageEvent) {
    this.productsMetaSignal;
    this.productsMetaSignal.update((prev) => {
      return {
        ...prev,
        page: e.pageIndex,
      };
    });
  }

  constructor(private readonly dialog: MatDialog) {
    effect(() => {
      const query = this.productsSignal();
      if (query) {
        this.products = query.data?.data ?? [];
        this.meta = query.data?.meta ?? null;
        this.loading = query.loading;
      }
    });
  }

  displayedColumns: string[] = [
    'id',
    'name',
    'estimatedEffort',
    'img',
    'actions',
  ];

  onUpdateProduct(product: ProductResponse) {
    this.dialog.open(MutateProductDialog, {
      data: product,
    });
  }

  openMutateProduct() {
    this.dialog.open(MutateProductDialog, {
      data: {},
    });
  }

  openDeleteProduct(id: string) {
    this.dialog.open(DeleteProductDialog, { data: { id } });
  }

  onSearchInput(e: Event) {
    this.productsMetaSignal.update((prev) => {
      return {
        ...prev,
        search: (e.target as HTMLInputElement).value,
      };
    });
  }
}
