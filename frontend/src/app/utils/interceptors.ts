import { HttpEvent, HttpHandlerFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { Observable } from 'rxjs';
import { TokenService } from '../services/token.service';

export function constructRequestInterceptor(
  req: HttpRequest<unknown>,
  next: HttpHandlerFn
): Observable<HttpEvent<unknown>> {
  const baseUrl = 'http://localhost:8080/api';
  const token = inject(TokenService).getToken();
  const newReq = req.clone({
    headers: req.headers
      .append('Content-Type', 'application/json')
      .append('Authorization', `Bearer ${token}`)
      .append('accept', '*/*'),
    url: baseUrl.concat(req.url),
  });

  return next(newReq);
}
