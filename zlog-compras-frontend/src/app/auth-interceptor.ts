// src/app/auth-interceptor.ts

import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('access_token');
  const loginUrl = 'http://localhost:8080/api/auth/login';

  // Ignora a requisição de login para evitar loops infinitos
  if (req.url === loginUrl) {
    return next(req);
  }

  if (token) {
    // Clona a requisição e adiciona o cabeçalho de autorização
    const authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(authReq);
  }

  return next(req);
};
