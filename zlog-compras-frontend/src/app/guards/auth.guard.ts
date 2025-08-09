// src/app/guards/auth.guard.ts
import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const token = localStorage.getItem('access_token');

  if (token) {
    return true; // Permite o acesso se o token existir
  } else {
    // Se não houver token, redireciona para a página de login
    return router.createUrlTree(['/login']);
  }
};
