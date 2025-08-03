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
  styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent {
  username: string = '';
  password: string = '';

  @Output() submitForm = new EventEmitter<{ username: string, password: string }>();

  constructor() { }

  onSubmit(): void {
    console.log('1. [LoginFormComponent] onSubmit() chamado. Dados:', { username: this.username, password: this.password }); 
    this.submitForm.emit({ username: this.username, password: this.password });
    this.username = '';
    this.password = '';
  }
}
