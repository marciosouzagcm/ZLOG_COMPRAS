// src/app/services/auth.service.ts
import { HttpClient, HttpHeaders } from '@angular/common/http'; // <-- Importe HttpClient e HttpHeaders
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs'; // <-- Importe Observable e tap
import { AuthResponse } from '../models/auth-response.model'; // <-- Vamos criar este modelo em breve
import { UserCredentials } from '../models/user-credentials.model'; // <-- Vamos criar este modelo em breve

@Injectable({
  providedIn: 'root' // Isso faz com que o serviço esteja disponível em toda a aplicação
})
export class AuthService {
  // **IMPORTANTE**: Altere esta URL para o endereço real do seu backend Spring Boot
  private apiUrl = 'http://localhost:8080/api/auth'; // Exemplo de endpoint de autenticação

  constructor(private http: HttpClient) { }

  login(credentials: UserCredentials): Observable<AuthResponse> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials, { headers })
      .pipe(
        tap(response => {
          // Simulação de armazenamento do token JWT após o login bem-sucedido
          console.log('Login bem-sucedido! Token JWT (simulado) recebido:', response.token);
          localStorage.setItem('jwt_token', response.token); // Armazena o token no localStorage
        })
      );
  }

  // Método simples para verificar se o usuário está logado (pela presença do token)
  isLoggedIn(): boolean {
    return !!localStorage.getItem('jwt_token');
  }

  // Método para remover o token ao fazer logout
  logout(): void {
    localStorage.removeItem('jwt_token');
    console.log('Logout realizado.');
    // Em uma aplicação real, você também redirecionaria para a tela de login
  }
}
