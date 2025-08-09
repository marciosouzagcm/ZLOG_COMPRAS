// src/app/components/product-list/product-list.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductService } from '../../services/product.service';
import { Produto } from '../../models/produto.model'; // AQUI: Mudamos o nome do modelo

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.css'
})
export class ProductListComponent implements OnInit {
  products: Produto[] = []; // AQUI: Definimos o tipo como Produto[]

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    this.productService.getProducts().subscribe({
      next: (data: Produto[]) => { // AQUI: Usamos o tipo Produto[]
        this.products = data;
        console.log('Produtos carregados:', this.products);
      },
      error: (error: any) => { // AQUI: Adicionamos o tipo any
        console.error('Erro ao buscar produtos:', error);
      }
    });
  }
}
