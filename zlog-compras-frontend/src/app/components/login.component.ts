// src/app/components/login.component.ts
import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { LoginFormComponent } from './login-form.component';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    LoginFormComponent
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'] // <-- MUDAR PARA .css AQUI
})
export class LoginComponent {
  errorMessage: string = '';

  constructor() { }

  handleLoginSubmit(credentials: { email: string, password: string }): void {
    console.log('Credenciais recebidas no LoginComponent:', credentials);
    this.errorMessage = '';

    if (credentials.email === 'teste@email.com' && credentials.password === 'senha123') {
      console.log('Login bem-sucedido!');
    } else {
      this.errorMessage = 'Usuário ou senha inválidos. Tente novamente.';
      console.log('Falha no login.');
    }
  }
}
