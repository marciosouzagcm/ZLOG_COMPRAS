// src/app/components/product-form/product-form.component.ts
import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Produto } from '../../models/produto.model';
import { ProdutoService } from '../../services/produto.service';

@Component({
  selector: 'app-product-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './product-form.component.html',
  styleUrl: './product-form.component.css'
})
export class ProductFormComponent implements OnInit {
  productForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private ProdutoService: ProdutoService,
    private router: Router
  ) {
    this.productForm = this.fb.group({
      codigo: ['', Validators.required],
      codigoProduto: ['', Validators.required],
      nome: ['', Validators.required],
      descricao: ['', Validators.required],
      unidadeMedida: ['', Validators.required],
      precoUnitario: [null, [Validators.required, Validators.min(0.01)]],
      categoria: ['', Validators.required],
      estoque: [null, [Validators.required, Validators.min(0)]]
    });
  }

  ngOnInit(): void {
    console.log('Formulário criado. Estado inicial:', this.productForm.value);
  }

  onSubmit(): void {
    if (this.productForm.valid) {
      console.log('Formulário de produto válido. Enviando dados...');
      const produtoParaEnviar: Produto = {
        ...this.productForm.value,
        precoUnitario: Number(this.productForm.value.precoUnitario),
        estoque: Number(this.productForm.value.estoque)
      };

      this.ProdutoService.createProduto(produtoParaEnviar).subscribe({
        next: (response: any) => {
          console.log('Produto criado com sucesso!', response);
          this.router.navigate(['/products']);
        },
        error: (error: any) => {
          console.error('Erro ao criar produto:', error);
          alert('Erro ao criar produto. Verifique o console para mais detalhes.');
        }
      });
    } else {
      console.error('Formulário de produto inválido. Não é possível enviar.');
      alert('Por favor, preencha todos os campos obrigatórios corretamente.');
    }
  }
}
