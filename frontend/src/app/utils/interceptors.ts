import { HttpEvent, HttpHandlerFn, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';

export function constructRequestInterceptor(
  req: HttpRequest<unknown>,
  next: HttpHandlerFn
): Observable<HttpEvent<unknown>> {
  const baseUrl = 'http://localhost:8080/api';
  const newReq = req.clone({
    headers: req.headers.append('Content-Type', 'application/json'),
    url: baseUrl.concat(req.url),
  });

  return next(newReq);
}
