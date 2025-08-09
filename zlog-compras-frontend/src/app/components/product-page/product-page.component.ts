// src/app/components/product-page/product-page.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductService } from '../../services/product.service';
import { Produto } from '../../models/produto.model';

@Component({
  selector: 'app-product-page',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './product-page.component.html',
  styleUrls: ['./product-page.component.css']
})
export class ProductPageComponent implements OnInit {
  products: Produto[] = []; // AQUI: Nome corrigido para 'products'

  constructor(private productService: ProductService) { }

  ngOnInit(): void {
    this.getProducts(); // AQUI: Chamada corrigida para 'getProducts'
  }

  getProducts(): void { // AQUI: Nome do método corrigido para 'getProducts'
    this.productService.getProducts().subscribe({
      next: (data: Produto[]) => { // AQUI: Adicionado o tipo 'Produto[]'
        this.products = data;
        console.log('Produtos carregados:', this.products);
      },
      error: (error: any) => { // AQUI: Adicionado o tipo 'any'
        console.error('Erro ao carregar produtos:', error);
      }
    });
  }
}
