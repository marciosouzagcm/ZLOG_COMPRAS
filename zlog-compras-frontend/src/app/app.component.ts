// src/app/app.component.ts
import { Component } from '@angular/core';
// import { RouterOutlet } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { LoginComponent } from './components/login.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    // RouterOutlet,
    FormsModule,
    LoginComponent
  ],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'] // <-- MUDANÇA AQUI: de .scss para .css
})
export class AppComponent {
  title = 'zlog-compras-frontend';
}
