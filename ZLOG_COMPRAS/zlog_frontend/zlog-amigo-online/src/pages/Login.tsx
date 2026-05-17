// src/pages/Login.tsx

import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '@/services/api'; // Sua instância do Axios com o Interceptor
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";

// Define a estrutura mínima da resposta de login do backend (Spring Boot)
interface JwtResponse {
  token: string;
  type?: string;
  id?: number;
  username?: string;
  email?: string;
  roles?: string[];
}

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  
  const navigate = useNavigate();

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      // 1. FAZ A REQUISIÇÃO POST PARA O ENDPOINT /api/auth/login
      const response = await api.post<JwtResponse>('/auth/login', {
        username,
        password,
      });

      const token = response.data.token;
      
      if (token) {
        // 2. SALVA O TOKEN NO LOCAL STORAGE
        // IMPORTANTE: A chave 'jwtToken' DEVE ser a mesma que o seu Interceptor no api.ts procura.
        localStorage.setItem('jwtToken', token);
        
        // Opcional: Salvar outras informações (como roles)
        if (response.data.roles) {
            localStorage.setItem('userRoles', JSON.stringify(response.data.roles));
        }
        
        console.log("Login bem-sucedido. Token salvo.");

        // 3. REDIRECIONA PARA O DASHBOARD
        navigate('/'); 

      } else {
        setError('Token não recebido. Verifique o formato da resposta do backend.');
      }
      
    } catch (err: never) {
      console.error("Erro no login:", err);
      
      // Trata erros de autenticação (401 Unauthorized, geralmente)
      if (err.response && err.response.status === 401) {
        setError('Credenciais inválidas. Verifique seu usuário e senha.');
      } else {
        // Exibe uma mensagem genérica para outros erros de rede/servidor
        setError('Falha na conexão ou erro desconhecido. Tente novamente.');
      }
      
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-50">
      <Card className="w-full max-w-sm">
        <CardHeader>
          <CardTitle className="text-2xl">Acessar ZLOG Compras</CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleLogin} className="grid gap-4">
            <div className="grid gap-2">
              <Label htmlFor="username">Usuário (Username)</Label>
              <Input
                id="username"
                type="text"
                placeholder="Seu nome de usuário"
                required
                value={username}
                onChange={(e) => setUsername(e.target.value)}
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="password">Senha</Label>
              <Input
                id="password"
                type="password"
                required
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </div>
            
            {error && <p className="text-sm text-red-500">{error}</p>}

            <Button type="submit" className="w-full" disabled={loading}>
              {loading ? 'Entrando...' : 'Entrar'}
            </Button>
          </form>
        </CardContent>
      </Card>
    </div>
  );
};

export default Login;