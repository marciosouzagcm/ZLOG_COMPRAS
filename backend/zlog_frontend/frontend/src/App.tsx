import { Toaster as Sonner } from "@/components/ui/sonner";
import { TooltipProvider } from "@/components/ui/tooltip";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { BrowserRouter, Routes, Route } from "react-router-dom";

// Componentes de Página
import Index from "./pages/Index";
import Login from "./pages/Login";
import NotFound from "./pages/NotFound";

// Configuração do cliente do React Query para gerenciamento de cache e requisições
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: 1, // Tenta refazer a requisição apenas 1 vez em caso de falha (evita sobrecarregar o Spring Boot)
      refetchOnWindowFocus: false, // Evita disparar requisições automáticas cada vez que o usuário muda de aba
    },
  },
});

const App = () => (
  <QueryClientProvider client={queryClient}>
    <TooltipProvider>
      {/* Sonner assume o controle global de notificações (Toasts) de forma otimizada */}
      <Sonner position="top-right" richColors closeButton />
      
      <BrowserRouter>
        <Routes>
          {/* Rota Pública */}
          <Route path="/login" element={<Login />} />

          {/* Rota Principal (No futuro, adicionaremos a guarda de autenticação aqui) */}
          <Route path="/" element={<Index />} />
          
          {/* Rota de Fallback para páginas inexistentes */}
          <Route path="*" element={<NotFound />} />
        </Routes>
      </BrowserRouter>
    </TooltipProvider>
  </QueryClientProvider>
);

export default App;