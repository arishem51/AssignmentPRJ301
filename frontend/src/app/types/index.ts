export type HttpServerResponse<T> = {
  message: string;
  data: T;
};

export type UserResponse = {
  role: 'ADMIN' | 'USER';
  token: string;
  username: string;
};

export type PaginateResponse<T> = HttpServerResponse<{
  data: T[];
  meta: {
    page: number;
    pageSize: number;
    totalElements: number;
    totalPages: number;
  };
}>;

export type ProductResponse = {
  id: number;
  name: string;
  estimatedEffort: number;
  img: string;
};
