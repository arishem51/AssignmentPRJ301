import { Component, effect } from '@angular/core';
import { PaginateMetaResponse, PlanResponse } from '../../types';
import { PlanService } from '../../services/plan.service';
import { Container } from '../container/Container.component';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { formatDate } from '../../utils';

@Component({
  standalone: true,
  selector: 'app-plan',
  imports: [Container, MatTableModule, MatButtonModule],
  styles: `
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
  `,
  template: ` <app-container
    [loading]="loading"
    [title]="'Plans'"
    [search]="search"
    [meta]="meta"
    (onAdd)="handleAdd()"
    (onSearch)="handleSearch()"
    (onPage)="handlePage()"
  >
    <table mat-table [dataSource]="plans ?? []" class="mat-elevation-z8">
      <!-- ID Column -->
      <ng-container matColumnDef="id">
        <th mat-header-cell *matHeaderCellDef>ID</th>
        <td mat-cell *matCellDef="let plan">{{ plan.id }}</td>
      </ng-container>

      <!-- Name Column -->
      <ng-container matColumnDef="name">
        <th mat-header-cell *matHeaderCellDef>Name</th>
        <td mat-cell *matCellDef="let plan">{{ plan.name }}</td>
      </ng-container>

      <!-- Start Date Column -->
      <ng-container matColumnDef="startDate">
        <th mat-header-cell *matHeaderCellDef>Start Date</th>
        <td mat-cell *matCellDef="let plan">
          {{ getFormattedDate(plan.startDate) }}
        </td>
      </ng-container>

      <!-- End Date Column -->
      <ng-container matColumnDef="endDate">
        <th mat-header-cell *matHeaderCellDef>End Date</th>
        <td mat-cell *matCellDef="let plan">
          {{ getFormattedDate(plan.endDate) }}
        </td>
      </ng-container>

      <!-- Status Column -->
      <ng-container matColumnDef="status">
        <th mat-header-cell *matHeaderCellDef>Status</th>
        <td mat-cell *matCellDef="let plan">{{ plan.status }}</td>
      </ng-container>

      <!-- Actions Column -->
      <ng-container matColumnDef="actions">
        <th mat-header-cell *matHeaderCellDef>Actions</th>
        <td mat-cell *matCellDef="let plan">
          <div style="display:flex; gap:8px;justify-content:center">
            <button mat-flat-button color="primary" (click)="editPlan(plan)">
              Update
            </button>
            <button mat-raised-button color="warn" (click)="deletePlan(plan)">
              Delete
            </button>
          </div>
        </td>
      </ng-container>

      <!-- Define Header and Row -->
      <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
      <tr mat-row *matRowDef="let row; columns: displayedColumns"></tr>
    </table>
  </app-container>`,
})
export class PlanComponent {
  loading = true;
  plans: PlanResponse[] | null = null;
  meta: PaginateMetaResponse | null = null;
  search: string = '';

  handleSearch() {}
  handlePage() {}
  handleAdd() {}
  editPlan(plan: PlanResponse) {}
  deletePlan(plan: PlanResponse) {}

  getFormattedDate(date: string) {
    return formatDate(date);
  }

  constructor(private planService: PlanService) {
    effect(() => {
      const querySignal = this.planService.getQuerySignal();
      const queryMetaSignal = this.planService.getQueryMetaSignal();
      this.search = queryMetaSignal().search ?? '';
      const query = querySignal();
      if (query) {
        this.plans = query.data?.data ?? [];
        this.meta = query.data?.meta ?? null;
        this.loading = query.loading;
      }
    });
  }

  displayedColumns: string[] = [
    'id',
    'name',
    'startDate',
    'endDate',
    'status',
    'actions',
  ];
}
