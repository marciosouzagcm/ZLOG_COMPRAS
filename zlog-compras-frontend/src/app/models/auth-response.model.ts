// src/app/models/auth-response.model.ts
export interface AuthResponse {
  token: string; // O token JWT retornado pelo backend
  // Você pode adicionar outras propriedades que seu backend retornar, ex:
  // userId: number;
  // username: string;
}
