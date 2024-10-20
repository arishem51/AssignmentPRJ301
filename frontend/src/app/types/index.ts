export type HttpServerResponse<T> = {
  message: string;
  data: T;
};

export type UserResponse = {
  role: 'ADMIN' | 'USER';
  token: string;
  username: string;
};

export type PaginateMetaResponse = {
  page: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
};

export type PaginateResponse<T> = HttpServerResponse<{
  data: T[];
  meta: PaginateMetaResponse;
}>;

export type ProductResponse = {
  id: number;
  name: string;
  img: string;
};

export type PlanStatus = 'OPEN' | 'CLOSED';
export type PlanResponse = {
  id: number;
  startDate: string;
  endDate: string;
  name: string;
  status: PlanStatus;
};

export type TQuery<TData> = {
  loading: boolean;
  data: TData | null;
};
