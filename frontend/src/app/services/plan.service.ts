import { Injectable } from '@angular/core';
import { QueryService } from './query.service';
import { PlanResponse } from '../types';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class PlanService extends QueryService<PlanResponse> {
  constructor(http: HttpClient) {
    super(http, 'plans');
  }
}
