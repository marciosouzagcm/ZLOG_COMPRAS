import { HttpInterceptorFn } from '@angular/common/http';

export const AuthInterceptor: HttpInterceptorFn = (req, next) => {
  // 1. Obtém o token do localStorage
  const authToken = localStorage.getItem('access_token');

  // 2. Se o token existir, clona a requisição e adiciona o cabeçalho Authorization
  if (authToken) {
    const authReq = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${authToken}`)
    });
    // 3. Envia a requisição clonada (com o token) para o próximo handler
    return next(authReq);
  }

  // 4. Se não houver token, envia a requisição original
  return next(req);
};
