import { Component } from '@angular/core';
import { Products } from '../../components/products/Products.component';
import { PlanComponent } from '../../components/plan/Plan.component';
import { MatTabsModule } from '@angular/material/tabs';

@Component({
  standalone: true,
  imports: [Products, PlanComponent, MatTabsModule],
  styles: [
    `
      .tab-container {
        margin: 24px 0;
      }
    `,
  ],
  template: `
    <mat-tab-group
      class="tab-container"
      mat-stretch-tabs="false"
      mat-align-tabs="start"
    >
      <mat-tab label="Products"><app-products></app-products></mat-tab>
      <mat-tab label="Plans">
        <app-plan></app-plan>
      </mat-tab>
    </mat-tab-group>
  `,
})
export class ProductionPlan {}
