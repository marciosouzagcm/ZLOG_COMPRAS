import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api' 
});

// 1. Interceptor de Requisições
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('jwtToken'); 

    if (token) {
      // Importante: Algumas versões do Axios preferem a sintaxe direta
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 2. Interceptor de Resposta
api.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    // AJUSTE CRÍTICO: Se for 401 (Expirado) OU 403 (Acesso negado/Token Inválido)
    if (error.response && (error.response.status === 401 || error.response.status === 403)) {
      console.error("Erro de autenticação/permissão (401/403). Limpando sessão...");
      
      // Limpa tudo para garantir que o próximo login gere um token novo e limpo
      localStorage.clear(); 
      
      // Só redireciona se não estivermos já na página de login para evitar loop
      if (window.location.pathname !== '/login') {
        window.location.href = '/login';
      }
    }
    
    return Promise.reject(error);
  }
);

export default api;