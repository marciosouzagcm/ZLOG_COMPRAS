// src/app/services/produto.service.ts
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Produto } from '../models/produto.model';

@Injectable({
  providedIn: 'root'
})
export class ProdutoService {
  private apiUrl = 'http://localhost:8080/api/produtos';

  constructor(private http: HttpClient) {}

  getProdutos(): Observable<Produto[]> {
    return this.http.get<Produto[]>(this.apiUrl).pipe(
      catchError(this.handleError)
    );
  }

  createProduto(produto: Produto): Observable<Produto> {
    return this.http.post<Produto>(this.apiUrl, produto).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    if (error.status === 0) {
      // Ocorreu um erro no lado do cliente ou na rede.
      console.error('Ocorreu um erro na rede:', error.error);
    } else {
      // O backend retornou um código de resposta sem sucesso.
      console.error(
        `O backend retornou o código ${error.status}, corpo do erro: `, error.error);
    }
    // Retorna um Observable com uma mensagem de erro.
    return throwError(() => new Error('Algo de errado aconteceu. Por favor, tente novamente mais tarde.'));
  }
}

