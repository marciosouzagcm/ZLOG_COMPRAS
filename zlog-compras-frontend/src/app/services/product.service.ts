// src/app/services/product.service.ts

import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = 'http://localhost:8080/api/produtos';

  constructor(private http: HttpClient) {}

  getProducts(): Observable<any[]> {
    const token = localStorage.getItem('access_token');
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
    return this.http.get<any[]>(this.apiUrl, { headers });
  }

  // Novo método para criar um produto
  createProduct(produtoData: any): Observable<any> {
    const token = localStorage.getItem('access_token');
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
    return this.http.post<any>(this.apiUrl, produtoData, { headers });
  }
}
