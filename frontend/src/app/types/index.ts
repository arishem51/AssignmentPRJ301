export type HttpServerResponse<T> = {
  message: string;
  data: T;
};

export type UserResponse = {
  role: 'ADMIN' | 'USER';
  token: string;
  username: string;
};
