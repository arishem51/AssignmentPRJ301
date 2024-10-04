import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpServerResponse, UserResponse } from '../types';
import { TokenService } from './token.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private isAuthenticate = false;

  constructor(
    private router: Router,
    private http: HttpClient,
    private tokenService: TokenService
  ) {}

  login(username: string, password: string) {
    this.http
      .post<HttpServerResponse<UserResponse>>(
        '/auth/login',
        JSON.stringify({ username, password })
      )
      .subscribe({
        next: (response) => {
          const token = response.data.token;
          if (token) {
            this.tokenService.setToken(token);
            this.isAuthenticate = true;
            this.router.navigate(['/']);
          }
        },
      });
  }

  logout() {
    this.isAuthenticate = false;
    this.tokenService.removeToken();
    this.router.navigate(['/login']);
  }

  isLoggedIn() {
    if (this.tokenService.getToken()) {
      this.isAuthenticate = true;
    }
    return this.isAuthenticate;
  }
}
