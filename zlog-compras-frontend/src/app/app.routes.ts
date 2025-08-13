// src/app/app.routes.ts
import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { ProdutosComponent } from './components/produtos/produtos.component';
import { RegisterComponent } from './components/register/register.component';
import { NovoProdutoComponent } from './components/novo-produto/novo-produto.component'; // Importe o novo componente
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'produtos', component: ProdutosComponent, canActivate: [authGuard] },
    { path: 'produtos/novo', component: NovoProdutoComponent, canActivate: [authGuard] }, // Adicione a nova rota aqui
    { path: '', redirectTo: 'login', pathMatch: 'full' },
    { path: '**', redirectTo: 'login' }
];
