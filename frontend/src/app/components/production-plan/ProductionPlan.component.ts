import { Component } from '@angular/core';
import { Products } from '../products/Products.component';
import { MatTabsModule } from '@angular/material/tabs';

@Component({
  standalone: true,
  imports: [Products, MatTabsModule],
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
      <mat-tab label="Plans"> </mat-tab>
    </mat-tab-group>
  `,
})
export class ProductionPlan {}
