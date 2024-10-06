import { HttpClient } from '@angular/common/http';
import { Injectable, computed, effect, signal } from '@angular/core';
import {
  PaginateMetaResponse,
  PaginateResponse,
  ProductResponse,
  TQuery,
} from '../types';
import { debounceTime, switchMap } from 'rxjs';
import { toObservable } from '@angular/core/rxjs-interop';

export type ProductQuery = PaginateResponse<ProductResponse>['data'];
type ProductQueryMeta = Omit<
  PaginateMetaResponse,
  'totalElements' | 'totalPages'
> & {
  search?: string;
};
@Injectable({
  providedIn: 'root',
})
export class ProductsService {
  private productQuerySignal = signal<TQuery<ProductQuery> | null>(null);
  private productQueryMetaSignal = signal<ProductQueryMeta>({
    page: 0,
    pageSize: 5,
    search: '',
  });

  constructor(private http: HttpClient) {
    this.productQuerySignal.set({
      loading: true,
      data: null,
    });
    toObservable(this.productQueryMetaSignal)
      .pipe(
        debounceTime(300),
        switchMap((query) => this.fetchProducts(query))
      )
      .subscribe({
        next: (value) => {
          this.productQuerySignal.set({
            loading: false,
            data: value.data,
          });
        },
        error: () => {
          this.productQuerySignal.set({
            loading: false,
            data: null,
          });
        },
      });
  }

  private fetchProducts({ search, page, pageSize }: ProductQueryMeta) {
    return this.http.get<PaginateResponse<ProductResponse>>(
      `/products?pageSize=${pageSize}&page=${page}&search=${search}`
    );
  }
  refetchProduct() {
    this.fetchProducts(this.productQueryMetaSignal()).subscribe({
      next: (value) => {
        this.productQuerySignal.set({
          loading: false,
          data: value.data,
        });
      },
      error: () => {
        this.productQuerySignal.set({
          loading: false,
          data: null,
        });
      },
    });
  }

  getProductQuerySignal() {
    return this.productQuerySignal;
  }
  getProductQueryMetaSignal() {
    return this.productQueryMetaSignal;
  }

  create(params: Omit<ProductResponse, 'id'>) {
    return this.http.post<ProductResponse>('/products', JSON.stringify(params));
  }
  update(params: ProductResponse) {
    return this.http.put<ProductResponse>(
      `/products/${params.id}`,
      JSON.stringify(params)
    );
  }
  delete(id: string) {
    return this.http.delete<ProductResponse>(`/products/${id}`);
  }
}
