import { Component, inject, signal } from '@angular/core';
import { ProductsService } from '../../services/products.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle,
} from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

export interface AddProductDialogData {
  id: string;
}

@Component({
  standalone: true,
  imports: [
    MatButtonModule,
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    MatDialogClose,
    MatProgressSpinnerModule,
  ],
  template: ` <h2 mat-dialog-title>Delete Products</h2>
    <mat-dialog-content class="dialog-content-container">
      <span>Are you sure to delete this product!</span>
    </mat-dialog-content>
    <mat-dialog-actions>
      <button mat-button mat-dialog-close>Close</button>
      <button mat-button (click)="onDeleteProduct()" disabled="{{ loading }}">
        @if(loading){
        <mat-spinner [diameter]="20"></mat-spinner>
        }@else{
        <span>Save</span>
        }
      </button>
    </mat-dialog-actions>`,
})
export class DeleteProductDialog {
  constructor(private productService: ProductsService) {}

  private _snackBar = inject(MatSnackBar);
  private dialogRef = inject(MatDialogRef<DeleteProductDialog>);
  readonly loadingSignal = signal(false);
  readonly data = inject<AddProductDialogData>(MAT_DIALOG_DATA);

  loading = this.loadingSignal();
  openSnackBar(message: string, action: string) {
    this._snackBar.open(message, action);
  }

  onDeleteProduct() {
    this.loadingSignal.set(true);

    this.productService.delete(this.data.id).subscribe({
      next: () => {
        this.loadingSignal.set(false);
        this.openSnackBar('Success!', '');
        this.productService.getProducts();
        this.dialogRef.close();
      },
      error: () => {
        this.loadingSignal.set(false);
        this.openSnackBar('Error!', '');
      },
    });
  }
}
