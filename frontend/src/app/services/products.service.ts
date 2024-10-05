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
}
