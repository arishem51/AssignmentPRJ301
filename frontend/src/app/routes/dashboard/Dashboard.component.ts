import { Component } from '@angular/core';
import {
  MatSidenavContainer,
  MatSidenav,
  MatSidenavContent,
} from '@angular/material/sidenav';
import { MatIcon } from '@angular/material/icon';
import { MatNavList, MatListItem } from '@angular/material/list';
import { MatToolbar } from '@angular/material/toolbar';
import { Router, RouterOutlet } from '@angular/router';
import { MatButton } from '@angular/material/button';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'dashboard',
  standalone: true,
  imports: [
    RouterOutlet,
    MatSidenavContainer,
    MatSidenav,
    MatListItem,
    MatNavList,
    MatSidenavContent,
    MatToolbar,
    MatIcon,
    MatButton,
  ],
  styles: [
    `
      .sidenav-container {
        top: 68px;
      }
      .toolbar {
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        z-index: 1000;
        width: 100%;
        max-width: 100%;
      }

      /* Push content to the right using flexbox */
      .mat-toolbar {
        display: flex;
        justify-content: space-between;
        align-items: center;
      }

      .spacer {
        flex: 1 1 auto;
      }

      .toolbar-title {
        margin-left: 8px;
        font-size: 20px;
        font-weight: bold;
      }
      .sidebar {
        position: fixed;
        top: 68px;
        padding: 0 24px 0 12px;
      }
      .content {
        height: calc(100vh - 68px);
      }
    `,
  ],
  template: `
    <mat-toolbar color="primary" class="mat-elevation-z4 toolbar">
      <button mat-raised-button (click)="handleGoHome()">Home</button>
      <span class="spacer"></span>
      <button mat-flat-button (click)="handleNavigateGithub()">
        <mat-icon>code</mat-icon> GitHub
      </button>
      <button mat-button (click)="handleLogOut()">
        <mat-icon>exit_to_app</mat-icon> Logout
      </button>
    </mat-toolbar>
    <mat-sidenav-container class="sidenav-container">
      <mat-sidenav mode="side" opened class="sidebar">
        <mat-nav-list>
          <mat-list-item (click)="handleProducts()"
            >Production Plan</mat-list-item
          >
          <mat-list-item (click)="handleEmployee()">Workshop</mat-list-item>
        </mat-nav-list>
      </mat-sidenav>

      <mat-sidenav-content class="content">
        <router-outlet></router-outlet>
      </mat-sidenav-content>
    </mat-sidenav-container>
  `,
})
export class Dashboard {
  constructor(private router: Router, private authService: AuthService) {}
  handleGoHome() {
    this.router.navigate(['/home']);
  }
  handleNavigateGithub() {
    window.open('https://github.com/arishem51/AssignmentPRJ301');
  }
  handleLogOut() {
    this.authService.logout();
  }
  handleProducts() {
    this.router.navigate(['/production-plan']);
  }
  handleEmployee() {
    this.router.navigate(['/employee']);
  }
}
