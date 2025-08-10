// src/app/app.routes.ts

import { Routes } from '@angular/router';


import { AdicionarProdutoComponent } from './components/adicionar-produto/adicionar-produto.component';
import { LoginComponent } from './components/login/login.component';
import { authGuard } from './guards/auth.guard';
import { ListaProdutosComponent } from './components/product-list/lista-produtos.componente';

export const routes: Routes = [
    // Redireciona a página inicial para a tela de login
    { path: '', redirectTo: '/login', pathMatch: 'full' },

    // Rota para o componente de login
    { path: 'login', component: LoginComponent },

    // Rotas protegidas pela guarda de autenticação
    { path: 'produtos', component: ListaProdutosComponent, canActivate: [authGuard] },
    { path: 'adicionar-produto', component: AdicionarProdutoComponent, canActivate: [authGuard] },

    // Redireciona qualquer rota inválida para o login
    { path: '**', redirectTo: '/login' }
];
