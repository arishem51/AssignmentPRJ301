import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PaginateResponse, ProductResponse } from '../types';

@Injectable({
  providedIn: 'root',
})
export class ProductsService {
  constructor(private http: HttpClient) {}

  getProducts() {
    return this.http.get<PaginateResponse<ProductResponse>>('/products');
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
