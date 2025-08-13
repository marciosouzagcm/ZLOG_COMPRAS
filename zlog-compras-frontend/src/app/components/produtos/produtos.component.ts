// src/app/components/produtos/produtos.component.ts

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { ProductService } from '../../services/product.service'; // Importe o serviço

@Component({
  selector: 'app-produtos',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './produtos.component.html',
  styleUrl: './produtos.component.css'
})
export class ProdutosComponent implements OnInit {
  produtos: any[] = [];
  errorMessage: string = '';

  constructor(
    private router: Router,
    private productService: ProductService // Re-injete o ProductService aqui
  ) {}

  ngOnInit(): void {
    this.getProdutos(); // Chame o método para buscar os produtos no início
  }

  getProdutos(): void {
    this.productService.getProducts().subscribe({
      next: (data) => {
        this.produtos = data;
      },
      error: (error) => {
        this.errorMessage = 'Erro ao carregar produtos. Certifique-se de que a API está rodando.';
        console.error('Erro ao buscar produtos:', error);
      }
    });
  }

  logout(): void {
    localStorage.removeItem('access_token');
    this.router.navigate(['/login']);
  }
}
