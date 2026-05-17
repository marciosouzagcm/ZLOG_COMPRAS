import { Toaster } from "@/components/ui/toaster";
import { Toaster as Sonner } from "@/components/ui/sonner";
import { TooltipProvider } from "@/components/ui/tooltip";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { BrowserRouter, Routes, Route } from "react-router-dom";

// Componentes de Página
import Index from "./pages/Index";
import NotFound from "./pages/NotFound";
// 🚨 NOVO: Importa o componente de Login para a rota /login
import Login from "./pages/Login"; 

const queryClient = new QueryClient();

const App = () => (
 <QueryClientProvider client={queryClient}>
 <TooltipProvider>
 <Toaster />
 <Sonner />
 <BrowserRouter>
 <Routes>
  {/* Rota de Login Adicionada */}
 <Route path="/login" element={<Login />} />

 {/* Rota Principal (Dashboard) */}
<Route path="/" element={<Index />} />
 
 {/* ADD ALL CUSTOM ROUTES ABOVE THE CATCH-ALL "*" ROUTE */}
 <Route path="*" element={<NotFound />} />
 </Routes>
 </BrowserRouter>
 </TooltipProvider>
 </QueryClientProvider>
);

export default App;