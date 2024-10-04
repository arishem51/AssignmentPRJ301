import { Component, OnInit } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ProductsService } from '../../services/products.service';
import { ProductResponse } from '../../types';

@Component({
  standalone: true,
  imports: [MatTableModule, MatProgressSpinnerModule],
  styles: [
    `
      .container {
        padding: 24px;
      }
      /* Table container styling */
      table {
        width: 100%;
        border-collapse: collapse;
      }

      .mat-elevation-z8 {
        margin-top: 20px;
        border-radius: 8px;
        overflow: hidden;
      }

      /* Table header styling */
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

      /* Table row styling */
      td.mat-cell {
        padding: 16px;
        font-size: 14px;
        border-bottom: 1px solid #e0e0e0;
      }

      /* Add hover effect for rows */
      tr.mat-row:hover {
        background-color: rgba(63, 81, 181, 0.08);
      }

      /* Style for alternate rows (zebra striping effect) */
      tr.mat-row:nth-child(even) td {
        background-color: #f5f5f5;
      }

      /* Table header row */
      tr.mat-header-row {
        height: 56px;
      }

      /* Style for the table rows */
      tr.mat-row {
        height: 48px;
      }

      /* Add subtle box shadow to the table */
      table {
        box-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);
      }

      /* Optional: Add rounded corners to the table */
      table {
        border-radius: 8px;
        overflow: hidden;
      }

      /* Optional: Add padding between the columns */
      td.mat-cell,
      th.mat-header-cell {
        padding: 12px 16px;
      }

      /* Optional: Change row color on selection */
      tr.mat-row.mat-row-selected {
        background-color: rgba(63, 81, 181, 0.1);
      }
      .loading-container {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100%;
      }
      td,
      th {
        text-align: center;
        vertical-align: middle;
      }
      td {
        padding: 12px 0;
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

        <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
        <tr mat-row *matRowDef="let row; columns: displayedColumns"></tr>
      </table>
    </div>
    }
  `,
})
export class Products implements OnInit {
  loading = true;
  products: ProductResponse[] = [];

  constructor(private productService: ProductsService) {}
  displayedColumns: string[] = ['id', 'name', 'estimatedEffort', 'img'];

  ngOnInit(): void {
    this.productService.getProducts().subscribe({
      next: (value) => {
        this.products = value;
      },
      error: () => {
        this.loading = false;
      },
      complete: () => {
        this.loading = false;
      },
    });
  }
}
