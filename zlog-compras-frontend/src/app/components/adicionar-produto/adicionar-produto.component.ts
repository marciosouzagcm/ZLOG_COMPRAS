import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ProdutoService } from '../../services/produto.service';

@Component({
  selector: 'app-adicionar-produto',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './adicionar-produto.component.html',
  styleUrls: ['./adicionar-produto.component.css']
})
export class AdicionarProdutoComponent implements OnInit {

  produtoForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private produtoService: ProdutoService,
    private router: Router
  ) {
    this.produtoForm = this.fb.group({
      nome: ['', Validators.required],
      codigo: ['', Validators.required],
      descricao: [''],
      precoUnitario: [null, [Validators.required, Validators.min(0)]],
      estoque: [null, [Validators.required, Validators.min(0)]]
    });
  }

  ngOnInit(): void {
  }

  salvarProduto(): void {
    if (this.produtoForm.valid) {
      this.produtoService.createProduto(this.produtoForm.value).subscribe(
        (response) => {
          console.log('Produto adicionado com sucesso:', response);
          this.router.navigate(['/produtos']); // Redireciona para a lista de produtos
        },
        (error) => {
          console.error('Erro ao adicionar produto:', error);
        }
      );
    }
  }
}
