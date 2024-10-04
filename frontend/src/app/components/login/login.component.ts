import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'login',
  standalone: true,
  imports: [FormsModule, MatInputModule, MatButtonModule],
  styles: [
    `
      .login-container {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        height: 100vh;
        padding: 20px;
        background-color: #f4f7fa;
      }

      .full-width {
        width: 100%;
        max-width: 300px;
        margin-bottom: 15px;
      }
    `,
  ],
  template: `
    <div class="login-container">
      <h1>Login</h1>
      <mat-form-field appearance="fill" class="full-width">
        <mat-label>Username</mat-label>
        <input
          matInput
          [(ngModel)]="input.username"
          (ngModelChange)="onUsernameChange($event)"
        />
      </mat-form-field>
      <mat-form-field appearance="fill" class="full-width">
        <mat-label>Password</mat-label>
        <input
          matInput
          [(ngModel)]="input.password"
          (ngModelChange)="onPasswordChange($event)"
          type="password"
        />
      </mat-form-field>
      <button mat-raised-button color="primary" class="full-width">
        Login
      </button>
    </div>
  `,
})
export class Login {
  input = { username: '', password: '' };
  onPasswordChange(value: string) {
    console.log(value);
  }
  onUsernameChange(value: string) {
    console.log(value);
  }
}
