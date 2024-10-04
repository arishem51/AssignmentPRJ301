import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'login',
  standalone: true,
  imports: [FormsModule],
  template: `
    <div>
      <h1>Login</h1>
      <label>Username: </label>
      <input
        type="text"
        [(ngModel)]="input.username"
        (ngModelChange)="onUsernameChange($event)"
      />
      <br />
      <br />
      <label>Password: </label>
      <input
        type="password"
        [(ngModel)]="input.password"
        (ngModelChange)="onPasswordChange($event)"
      />
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
