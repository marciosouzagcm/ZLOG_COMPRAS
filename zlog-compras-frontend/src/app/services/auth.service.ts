// src/app/services/auth.service.ts
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { AuthResponse } from '../models/auth-response.model';
import { UserCredentials } from '../models/user-credentials.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) { }

  login(credentials: UserCredentials, password: any): Observable<AuthResponse> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials, { headers })
      .pipe(
        tap(response => {
          console.log('Login bem-sucedido! Token JWT recebido:', response.token);
          // AQUI: Usamos a mesma chave que o AuthGuard
          localStorage.setItem('access_token', response.token);
        })
      );
  }

  isLoggedIn(): boolean {
    // AQUI: Verificamos a mesma chave
    return !!localStorage.getItem('access_token');
  }

  logout(): void {
    // AQUI: Removemos a mesma chave
    localStorage.removeItem('access_token');
    console.log('Logout realizado.');
  }
}
