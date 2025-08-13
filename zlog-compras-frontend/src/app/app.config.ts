// src/app/app.config.ts

import { provideHttpClient, withInterceptors } from '@angular/common/http'; // Importe estes módulos
import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { authInterceptor } from './auth-interceptor'; // Importe o seu interceptor

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    // Registra o interceptor na aplicação
    provideHttpClient(withInterceptors([authInterceptor]))
  ]
};
