import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

@Injectable({
  providedIn: 'root',
})
export class TokenService {
  private tokenKey = 'TOKEN_KEY';
  private isBrowser: boolean;

  constructor(@Inject(PLATFORM_ID) private platformId: Object) {
    // Check if the current platform is the browser
    this.isBrowser = isPlatformBrowser(this.platformId);
  }

  setToken(token: string) {
    if (this.isBrowser) {
      localStorage.setItem(this.tokenKey, token);
    }
  }

  removeToken() {
    if (this.isBrowser) {
      localStorage.removeItem(this.tokenKey);
    }
  }

  getToken() {
    if (this.isBrowser) {
      return localStorage.getItem(this.tokenKey);
    }
    return null;
  }
}
