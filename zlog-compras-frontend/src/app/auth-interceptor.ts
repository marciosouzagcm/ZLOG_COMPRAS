import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('access_token');
  const loginUrl = 'http://localhost:8080/auth/login'; // Ou a sua URL de login

  // Não adiciona o token se a requisição for para a URL de login
  if (req.url === loginUrl) {
    return next(req);
  }

  if (token) {
    const cloned = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token}`)
    });
    return next(cloned);
  }

  return next(req);
};
