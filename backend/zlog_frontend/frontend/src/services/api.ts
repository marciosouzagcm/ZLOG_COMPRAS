import axios, { InternalAxiosRequestConfig, AxiosResponse, AxiosError } from 'axios';

// Instância centralizada do Axios configurada para o backend Spring Boot
const api = axios.create({
  baseURL: 'http://localhost:8080/api', // Garanta que seu Spring Boot use /api como prefixo global
  timeout: 10000, // Termina a requisição após 10 segundos caso o servidor esteja fora do ar
  headers: {
    'Content-Type': 'application/json',
  }
});

// 1. Interceptor de Requisições: Injeta o Token JWT de forma proativa
api.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const token = localStorage.getItem('jwtToken'); 

    if (token && config.headers) {
      // Sintaxe padrão de mercado segura para o TypeScript e Axios moderno
      config.headers.Authorization = `Bearer ${token}`;
    }
    
    return config;
  },
  (error: AxiosError) => {
    return Promise.reject(error);
  }
);

// 2. Interceptor de Respostas: Monitora e reage a falhas globais de segurança
api.interceptors.response.use(
  (response: AxiosResponse) => {
    return response;
  },
  (error: AxiosError) => {
    // Captura erros de credenciais expiradas (401) ou falta de privilégio de acesso (403)
    if (error.response && (error.response.status === 401 || error.response.status === 403)) {
      console.warn("Sessão inválida ou expirada (401/403). Expulsando usuário de forma segura...");
      
      // Limpa chaves de segurança para forçar reautenticação
      localStorage.removeItem('jwtToken');
      localStorage.removeItem('userRoles');
      
      // Redirecionamento seguro para evitar loops de carregamento
      if (window.location.pathname !== '/login') {
        window.location.href = '/login';
      }
    }
    
    return Promise.reject(error);
  }
);

export default api;