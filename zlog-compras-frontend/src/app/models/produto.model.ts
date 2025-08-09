// src/app/models/produto.model.ts
export interface Produto {
  id?: number; // Opcional, pois é gerado pelo backend
  codigo: string;
  codigoProduto: string;
  nome: string;
  descricao: string;
  unidadeMedida: string;
  precoUnitario: number;
  categoria: string;
  estoque: number;
}
