import {
  ChangeDetectionStrategy,
  Component,
  inject,
  signal,
} from '@angular/core';
import { ProductResponse } from '../../types';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle,
} from '@angular/material/dialog';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ProductsService } from '../../services/products.service';
import { MatSnackBar } from '@angular/material/snack-bar';

export interface MutateProductDialogData extends Omit<ProductResponse, 'id'> {
  id?: string;
}

@Component({
  selector: 'add-product-dialog',
  styles: [
    `
      .dialog-content-container {
        display: flex;
        flex-direction: column;
      }
      mat-form-field {
        width: 332px;
      }
    `,
  ],
  template: `
    <h2 mat-dialog-title>Add Products</h2>
    <mat-dialog-content class="dialog-content-container">
      <mat-form-field>
        <mat-label>Product Name</mat-label>
        <input matInput [(ngModel)]="name" />
      </mat-form-field>

      <mat-form-field>
        <mat-label>Image URL</mat-label>
        <input matInput [(ngModel)]="img" />
      </mat-form-field>
    </mat-dialog-content>
    <mat-dialog-actions>
      <button mat-button mat-dialog-close>Close</button>
      <button mat-button (click)="onMutateProduct()" disabled="{{ loading }}">
        @if(loading){
        <mat-spinner [diameter]="20"></mat-spinner>
        }@else{
        <span>Save</span>
        }
      </button>
    </mat-dialog-actions>
  `,
  standalone: true,
  imports: [
    MatFormFieldModule,
    MatInputModule,
    FormsModule,
    MatButtonModule,
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    MatDialogClose,
    MatProgressSpinnerModule,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class MutateProductDialog {
  constructor(private productService: ProductsService) {}

  readonly data = inject<MutateProductDialogData>(MAT_DIALOG_DATA);
  readonly name = signal(this.data.name ?? '');
  readonly img = signal(this.data.img ?? '');
  readonly loadingSignal = signal(false);
  loading = this.loadingSignal();

  private _snackBar = inject(MatSnackBar);
  private dialogRef = inject(MatDialogRef<MutateProductDialog>);

  openSnackBar(message: string, action: string) {
    this._snackBar.open(message, action);
  }

  onMutateProduct() {
    this.loadingSignal.set(true);
    if (this.name() && this.img()) {
      const products = {
        name: this.name(),
        img: this.img(),
      };
      const request = this.data.id
        ? this.productService.update({
            ...products,
            id: +this.data.id,
          })
        : this.productService.create(products);
      request.subscribe({
        next: () => {
          this.productService.refetchProduct();
          this.loadingSignal.set(false);
          this.openSnackBar('Success!', '');
          this.dialogRef.close();
        },
        error: () => {
          this.loadingSignal.set(false);
          this.openSnackBar('Error!', '');
        },
      });
    }
  }
}
