// src/app/components/login.component.ts
import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router'; // 1. Importe o Router
import { UserCredentials } from '../models/user-credentials.model';
import { AuthService } from '../services/auth.service';
import { LoginFormComponent } from './login-form.component';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    LoginFormComponent
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  errorMessage: string = '';

  // 2. Injete o AuthService e o Router no construtor
  constructor(private authService: AuthService, private router: Router) { }

  handleLoginSubmit(credentials: UserCredentials): void {
    console.log('1. [LoginComponent] Tentando login com credenciais:', credentials);
    this.errorMessage = '';

    this.authService.login(credentials).subscribe({
      next: (response) => {
        console.log('2. [LoginComponent] Login bem-sucedido! Redirecionando...');

        // 3. Redirecione o usuário para a página de produtos
        this.router.navigate(['/products']);
      },
      error: (error) => {
        console.error('2. [LoginComponent] Erro no login:', error);
        if (error.status === 401) {
          this.errorMessage = 'Credenciais inválidas. Verifique seu email e senha.';
        } else {
          this.errorMessage = 'Ocorreu um erro no servidor. Tente novamente mais tarde.';
        }
      }
    });
  }
}
