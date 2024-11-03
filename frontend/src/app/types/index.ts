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
type Shift = {
  id: number;
  name: string;
  startTime: string;
  endTime: string;
};

type ScheduleCampaign = {
  id: number;
  quantity: number;
  date: string;
  endDate: string;
  shifts: Shift[];
};

export type PlanCampaign = {
  id: number;
  product: ProductResponse;
  quantity: number;
  estimateEffort: number;
  scheduleCampaigns: ScheduleCampaign[];
};

export type PlanResponse = {
  id: number;
  startDate: string;
  endDate: string;
  name: string;
  status: PlanStatus;
  planCampaigns?: PlanCampaign[];
};

export type TQuery<TData> = {
  loading: boolean;
  data: TData | null;
};
