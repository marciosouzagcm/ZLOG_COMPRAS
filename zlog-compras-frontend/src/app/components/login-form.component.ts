// src/app/components/login-form.component.ts
import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login-form',
  standalone: true,
  imports: [
    FormsModule
  ],
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css'] // <-- MUDAR PARA .css AQUI
})
export class LoginFormComponent {
  email: string = '';
  password: string = '';

  @Output() submitForm = new EventEmitter<{ email: string, password: string }>();

  constructor() { }

  onSubmit(): void {
    this.submitForm.emit({ email: this.email, password: this.password });
    this.email = '';
    this.password = '';
  }
}
