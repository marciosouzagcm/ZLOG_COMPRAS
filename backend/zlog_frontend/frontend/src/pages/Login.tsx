import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { AxiosError } from 'axios';
import api from '@/services/api';
import { toast } from 'sonner';

// Componentes de UI do shadcn/ui
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";

// 1. Definição do Esquema de Validação com Zod
const loginSchema = z.object({
  username: z.string().min(3, { message: "O usuário deve ter pelo menos 3 caracteres." }),
  password: z.string().min(4, { message: "A senha deve ter pelo menos 4 caracteres." }),
});

// Extração automática da tipagem do formulário através do esquema Zod
type LoginFormData = z.infer<typeof loginSchema>;

interface JwtResponse {
  token: string;
  type?: string;
  id?: number;
  username?: string;
  email?: string;
  roles?: string[];
}

const Login = () => {
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  // 2. Inicialização do React Hook Form integrado ao Zod
  const { register, handleSubmit, formState: { errors } } = useForm<LoginFormData>({
    resolver: zodResolver(loginSchema),
    defaultValues: {
      username: '',
      password: '',
    }
  });

  // 3. Processamento do Envio de Dados Autenticados
  const onSubmit = async (data: LoginFormData) => {
    setLoading(true);

    try {
      const response = await api.post<JwtResponse>('/auth/login', {
        username: data.username,
        password: data.password,
      });

      const { token, roles } = response.data;
      
      if (token) {
        localStorage.setItem('jwtToken', token);
        
        if (roles) {
          localStorage.setItem('userRoles', JSON.stringify(roles));
        }
        
        toast.success("Autenticação realizada com sucesso!");
        navigate('/'); 
      } else {
        toast.error("Resposta inválida do servidor. Token ausente.");
      }
      
    } catch (err) {
      // Tipagem segura do erro capturado no bloco catch para a assinatura do Axios
      const axiosError = err as AxiosError;
      console.error("Erro no processo de login:", axiosError);
      
      if (axiosError.response && axiosError.response.status === 401) {
        toast.error("Credenciais inválidas. Verifique seu usuário e senha.");
      } else {
        toast.error("Falha na conexão com o servidor. Tente novamente mais tarde.");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-50 px-4">
      <Card className="w-full max-w-sm shadow-md">
        <CardHeader className="space-y-1">
          <CardTitle className="text-2xl font-bold text-center">ZLOG Compras</CardTitle>
          <p className="text-sm text-gray-500 text-center">Insira suas credenciais para acessar o painel</p>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit(onSubmit)} className="grid gap-4">
            
            {/* Campo Usuário */}
            <div className="grid gap-2">
              <Label htmlFor="username">Usuário</Label>
              <Input
                id="username"
                type="text"
                placeholder="Seu nome de usuário"
                disabled={loading}
                {...register('username')}
                className={errors.username ? "border-red-500 focus-visible:ring-red-500" : ""}
              />
              {errors.username && (
                <p className="text-xs font-medium text-red-500">{errors.username.message}</p>
              )}
            </div>
            
            {/* Campo Senha */}
            <div className="grid gap-2">
              <Label htmlFor="password">Senha</Label>
              <Input
                id="password"
                type="password"
                placeholder="••••••••"
                disabled={loading}
                {...register('password')}
                className={errors.password ? "border-red-500 focus-visible:ring-red-500" : ""}
              />
              {errors.password && (
                <p className="text-xs font-medium text-red-500">{errors.password.message}</p>
              )}
            </div>

            {/* Botão de Ação Submeter */}
            <Button type="submit" className="w-full mt-2" disabled={loading}>
              {loading ? 'Autenticando...' : 'Entrar no Sistema'}
            </Button>
            
          </form>
        </CardContent>
      </Card>
    </div>
  );
};

export default Login;