// src/app/components/novo-produto/novo-produto.component.ts

import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { ProductService } from '../../services/product.service';

@Component({
  selector: 'app-novo-produto',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './novo-produto.component.html',
  styleUrls: ['./novo-produto.component.css']
})
export class NovoProdutoComponent {
  produtoForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private productService: ProductService,
    private router: Router
  ) {
    this.produtoForm = this.fb.group({
      codigo: ['', Validators.required],
      codigoProduto: ['', Validators.required],
      nome: ['', Validators.required],
      descricao: [''],
      unidadeMedida: ['', Validators.required],
      precoUnitario: ['', [Validators.required, Validators.min(0)]],
      categoria: ['', Validators.required],
      estoque: ['', [Validators.required, Validators.min(0)]],
    });
  }

  onSubmit(): void {
    if (this.produtoForm.valid) {
      this.productService.createProduct(this.produtoForm.value).subscribe({
        next: () => {
          alert('Produto adicionado com sucesso!');
          this.router.navigate(['/produtos']);
        },
        error: (error) => {
          console.error('Erro ao adicionar produto:', error);
          alert('Erro ao adicionar produto.');
        }
      });
    }
  }
}
