// src/app/app.config.server.ts
import { ApplicationConfig } from '@angular/core';
import { provideServerRendering } from '@angular/platform-server';
// Importe 'withFetch' junto com 'provideHttpClient'
import { provideHttpClient, withFetch } from '@angular/common/http';

const serverConfig: ApplicationConfig = {
  providers: [
    provideServerRendering(),
    // Adicione withFetch() aqui também para o ambiente do servidor (SSR)
    provideHttpClient(withFetch())
  ]
};

export const config = serverConfig;
