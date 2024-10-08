import { Component } from '@angular/core';
import { Products } from '../products/products.component';

@Component({
  standalone: true,
  imports: [Products],
  template: ` <app-products></app-products>`,
})
export class PlanComponent {}
