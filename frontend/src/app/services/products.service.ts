import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PaginateResponse, ProductResponse, TQuery } from '../types';
import { BehaviorSubject } from 'rxjs';

type ProductQuery = PaginateResponse<ProductResponse>['data'] | null;
@Injectable({
  providedIn: 'root',
})
export class ProductsService {
  private productQuerySubject = new BehaviorSubject<TQuery<
    NonNullable<ProductQuery>
  > | null>(null);

  constructor(private http: HttpClient) {}

  async getProducts() {
    this.productQuerySubject.next({
      loading: true,
      data: null,
    });
    // await new Promise((res) => setTimeout(() => res(1), 5000));
    this.fetchProducts();
  }

  fetchProducts() {
    this.http.get<PaginateResponse<ProductResponse>>('/products').subscribe({
      next: (value) => {
        this.productQuerySubject.next({
          loading: false,
          data: value.data,
        });
      },
      error: () => {
        this.productQuerySubject.next({
          loading: false,
          data: null,
        });
      },
    });
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
  getProductQuery() {
    return this.productQuerySubject.asObservable();
  }
}
