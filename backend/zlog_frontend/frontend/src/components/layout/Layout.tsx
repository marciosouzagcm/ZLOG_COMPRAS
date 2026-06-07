import { ReactNode, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Header } from "./Header";
import { Sidebar } from "./Sidebar";
import { cn } from "@/lib/utils";

interface LayoutProps {
  children: ReactNode;
  className?: string;
}

export function Layout({ children, className }: LayoutProps) {
  const navigate = useNavigate();
  const token = localStorage.getItem("jwtToken");

  // 1. Guarda de Rota: Expulsa usuários não autenticados antes da renderização das telas
  useEffect(() => {
    if (!token) {
      console.warn("Acesso não autorizado detectado. Redirecionando para o login...");
      navigate("/login", { replace: true });
    }
  }, [token, navigate]);

  // Se o token não existir, retorna um estado vazio (blank) enquanto o useEffect processa o redirecionamento
  if (!token) {
    return <div className="min-h-screen bg-slate-50" />;
  }

  return (
    <div className="min-h-screen bg-slate-50 dark:bg-background text-slate-900">
      {/* Topbar Fixa */}
      <Header />
      
      <div className="flex pt-16 h-[calc(100vh-4rem)]">
        
        {/* Painel de Navegação Lateral (Sidebar) com posicionamento responsivo */}
        <aside className="fixed left-0 top-16 z-30 w-64 h-[calc(100vh-4rem)] border-r border-slate-200 bg-white overflow-y-auto hidden md:block">
          <Sidebar />
        </aside>
        
        {/* Container do Conteúdo Principal - Adapta a margem dinamicamente baseado no breakpoint de tela */}
        <main className={cn("flex-1 min-w-0 md:ml-64 transition-all duration-200", className)}>
          <div className="container mx-auto p-4 md:p-6 lg:p-8 max-w-7xl animate-fade-in">
            {children}
          </div>
        </main>
        
      </div>
    </div>
  );
}