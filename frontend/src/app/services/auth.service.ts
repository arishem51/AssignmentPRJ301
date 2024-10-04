import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private isAuthenticate = false;

  constructor(private router: Router) {}

  login() {
    this.isAuthenticate = true;
  }

  logout() {
    this.isAuthenticate = false;
    this.router.navigate(['/login']);
  }

  isLoggedIn() {
    return this.isAuthenticate;
  }
}
