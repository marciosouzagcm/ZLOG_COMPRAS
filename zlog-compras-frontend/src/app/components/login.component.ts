// src/app/components/login.component.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginFormComponent } from './login-form.component';
import { AuthService } from '../services/auth.service'; // <-- Importe o AuthService
import { UserCredentials } from '../models/user-credentials.model'; // <-- Importe UserCredentials

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

  // Injete o AuthService no construtor
  constructor(private authService: AuthService) { }

  handleLoginSubmit(credentials: UserCredentials): void {
    console.log('1. [LoginComponent] Tentando login com credenciais:', credentials);
    this.errorMessage = ''; // Limpa qualquer mensagem de erro anterior

    // Chama o método login do AuthService
    this.authService.login(credentials).subscribe({
      next: (response) => {
        // Callback para sucesso: A API respondeu com sucesso, e o token foi armazenado
        console.log('2. [LoginComponent] Login REAL bem-sucedido! Token armazenado no localStorage.');
        // Em uma aplicação real, você redirecionaria o usuário aqui para a dashboard
        // Ex: this.router.navigate(['/dashboard']);
        alert('Login bem-sucedido! Verifique o console para o token.'); // Apenas para feedback visual imediato
      },
      error: (error) => {
        // Callback para erro: A API retornou um erro (ex: credenciais inválidas)
        console.error('2. [LoginComponent] Erro no login:', error);
        this.errorMessage = 'Usuário ou senha inválidos. Por favor, tente novamente.';
        // Você pode personalizar a mensagem de erro baseada no 'error.status' ou 'error.message' da resposta da API
        if (error.status === 401) {
            this.errorMessage = 'Credenciais inválidas. Verifique seu email e senha.';
        } else {
            this.errorMessage = 'Ocorreu um erro no servidor. Tente novamente mais tarde.';
        }
      }
    });
  }
}
