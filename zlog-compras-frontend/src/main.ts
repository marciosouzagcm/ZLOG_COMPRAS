// src/main.ts
import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
// Importe 'withFetch' junto com 'provideHttpClient'
import { provideHttpClient, withFetch } from '@angular/common/http';

bootstrapApplication(AppComponent, {
  providers: [
    // Adicione withFetch() aqui para otimizar o HttpClient
    provideHttpClient(withFetch())
  ]
}).catch(err => console.error(err));
