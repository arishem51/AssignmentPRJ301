import { HttpClient } from '@angular/common/http';
import { Inject, Injectable, signal } from '@angular/core';
import { PaginateMetaResponse, PaginateResponse, TQuery } from '../types';
import { debounceTime, switchMap } from 'rxjs';
import { toObservable } from '@angular/core/rxjs-interop';

export type Query<T> = PaginateResponse<T>['data'];
type QueryMeta = Omit<PaginateMetaResponse, 'totalElements' | 'totalPages'> & {
  search?: string;
};
@Injectable({
  providedIn: 'root',
})
export class QueryService<T extends { id: number }> {
  private querySignal = signal<TQuery<Query<T>> | null>(null);
  private queryMetaSignal = signal<QueryMeta>({
    page: 0,
    pageSize: 5,
    search: '',
  });

  constructor(
    private http: HttpClient,
    @Inject('BASE_PATH') private basePath: string
  ) {
    this.querySignal.set({
      loading: true,
      data: null,
    });
    toObservable(this.queryMetaSignal)
      .pipe(
        debounceTime(300),
        switchMap((query) => this.fetchData(query))
      )
      .subscribe({
        next: (value) => {
          this.querySignal.set({
            loading: false,
            data: value.data,
          });
        },
        error: () => {
          this.querySignal.set({
            loading: false,
            data: null,
          });
        },
      });
  }

  private fetchData({ search, page, pageSize }: QueryMeta) {
    return this.http.get<PaginateResponse<T>>(
      `/${this.basePath}?pageSize=${pageSize}&page=${page}&search=${search}`
    );
  }
  refetchProduct() {
    this.fetchData(this.queryMetaSignal()).subscribe({
      next: (value) => {
        this.querySignal.set({
          loading: false,
          data: value.data,
        });
      },
      error: () => {
        this.querySignal.set({
          loading: false,
          data: null,
        });
      },
    });
  }

  getQuerySignal() {
    return this.querySignal;
  }
  getQueryMetaSignal() {
    return this.queryMetaSignal;
  }

  create(params: Omit<T, 'id'>) {
    return this.http.post<T>(`/${this.basePath}`, JSON.stringify(params));
  }
  update(params: T) {
    return this.http.put<T>(
      `/${this.basePath}/${params.id}`,
      JSON.stringify(params)
    );
  }
  delete(id: string) {
    return this.http.delete<T>(`/${this.basePath}/${id}`);
  }
}
