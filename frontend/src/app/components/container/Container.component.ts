import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTableModule } from '@angular/material/table';
import { PaginateMetaResponse } from '../../types';

@Component({
  selector: 'app-container',
  standalone: true,
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
      <h5 style="text-align:center">{{ title }}</h5>
      <div class="search-container">
        <button mat-flat-button (click)="onAddClick()">Add</button>
        <mat-form-field class="example-form-field">
          <mat-label>Search</mat-label>
          <input
            matInput
            type="text"
            [value]="search"
            (input)="onSearchInput($event)"
          />
        </mat-form-field>
        <div>
          <mat-paginator
            [length]="meta?.totalElements"
            [pageSize]="meta?.pageSize ?? 0"
            [pageSizeOptions]="[meta?.pageSize ?? 0]"
            (page)="onPageChange($event)"
            aria-label="Select page"
          >
          </mat-paginator>
        </div>
      </div>
      <ng-content></ng-content>
    </div>
    }
  `,
})
export class Container {
  @Input() loading: boolean = false;
  @Input() title: string = 'Title';

  @Input() search: string = '';
  @Input() meta: PaginateMetaResponse | null = null;

  @Output() onAdd: EventEmitter<void> = new EventEmitter<void>();
  @Output() onSearch: EventEmitter<string> = new EventEmitter<string>();
  @Output() onPage: EventEmitter<PageEvent> = new EventEmitter<PageEvent>();

  onAddClick() {
    this.onAdd.emit();
  }

  onSearchInput(event: Event) {
    const inputElement = event.target as HTMLInputElement;
    this.onSearch.emit(inputElement.value);
  }

  onPageChange(event: PageEvent) {
    this.onPage.emit(event);
  }
}
