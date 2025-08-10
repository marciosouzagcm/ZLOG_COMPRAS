import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { bootstrapApplication } from '@angular/platform-browser';
import { provideRouter } from '@angular/router';
import { AppComponent } from './app/app.component';
import { routes } from './app/app.routes';
import { authInterceptor } from './app/auth-interceptor'; // <-- Caminho corrigido

bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(routes),
    // O 'provideHttpClient' agora é configurado com 'withInterceptors' para usar o nosso interceptor.
    // Isso é a forma correta e mais simples de se fazer em aplicações 'standalone'.
    provideHttpClient(withInterceptors([authInterceptor]))
  ]
}).catch(err => console.error(err));
