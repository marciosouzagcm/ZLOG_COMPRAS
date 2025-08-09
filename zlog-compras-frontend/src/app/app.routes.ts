// src/app/app.routes.ts
import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';

// O caminho correto para o LoginComponent
import { LoginComponent } from './components/login.component'; // <-- CORRIGIDO
import { ProductListComponent } from './components/product-list/product-list.component';
import { ProductFormComponent } from './components/product-form/product-form.component';

export const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'products',
    canActivate: [authGuard],
    component: ProductListComponent
  },
  {
    path: 'products/add', // Nova rota para o formulário
    canActivate: [authGuard],
    component: ProductFormComponent
  },
  {
    path: '',
    redirectTo: '/products',
    pathMatch: 'full'
  },
  // Adicione outras rotas aqui, como a página 404
];
