import { Button } from "@/components/ui/button";
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from "@/components/ui/collapsible";
import { cn } from "@/lib/utils";
import {
  ChevronDown,
  ChevronRight,
  FileText,
  LayoutDashboard,
  Package,
  Settings,
  ShoppingCart,
  TrendingUp,
  Users,
} from "lucide-react";
import { ComponentType, useState } from "react";
import { Link, useLocation } from "react-router-dom";

interface SidebarProps {
  className?: string;
}

interface NavItem {
  title: string;
  icon: ComponentType<{ className?: string }>; // Tipagem estrita para componentes de ícones Lucide
  href?: string;
  children?: NavItem[];
  badge?: string;
  roles?: string[]; // Define quais perfis têm acesso visual a este item do menu
}

const navigationItems: NavItem[] = [
  {
    title: "Dashboard",
    icon: LayoutDashboard,
    href: "/",
  },
  {
    title: "Solicitações",
    icon: FileText,
    children: [
      { title: "Todas as Solicitações", icon: FileText, href: "/solicitacoes" },
      { title: "Minhas Solicitações", icon: FileText, href: "/minhas-solicitacoes" },
      { title: "Aguardando Aprovação", icon: FileText, href: "/aprovacoes", badge: "5", roles: ["ROLE_ADMIN"] },
    ],
  },
  {
    title: "Cotações",
    icon: TrendingUp,
    children: [
      { title: "Orçamentos", icon: TrendingUp, href: "/orcamentos" },
      { title: "Comparativo", icon: TrendingUp, href: "/comparativo", roles: ["ROLE_ADMIN"] },
    ],
  },
  {
    title: "Pedidos de Compra",
    icon: ShoppingCart,
    children: [
      { title: "Todos os Pedidos", icon: ShoppingCart, href: "/pedidos" },
      { title: "Em Andamento", icon: ShoppingCart, href: "/pedidos/andamento", badge: "12" },
      { title: "Recebidos", icon: Package, href: "/pedidos/recebidos" },
    ],
  },
  {
    title: "Fornecedores",
    icon: Users,
    href: "/fornecedores",
    roles: ["ROLE_ADMIN"], // Apenas administradores gerenciam fornecedores cadastrados
  },
  {
    title: "Produtos",
    icon: Package,
    href: "/produtos",
  },
  {
    title: "Relatórios",
    icon: TrendingUp,
    roles: ["ROLE_ADMIN"], // Esconde toda a árvore de relatórios gerenciais de usuários normais
    children: [
      { title: "Analytics", icon: TrendingUp, href: "/relatorios/analytics" },
      { title: "Exportar Dados", icon: TrendingUp, href: "/relatorios/exportar" },
    ],
  },
];

export function Sidebar({ className }: SidebarProps) {
  const [openItems, setOpenItems] = useState<string[]>(["Solicitações"]);
  const location = useLocation();

  // 1. Recupera as permissões reais do usuário salvas no fluxo de login
  const getUserRoles = (): string[] => {
    try {
      const roles = localStorage.getItem("userRoles");
      return roles ? JSON.parse(roles) : ["ROLE_USER"];
    } catch {
      return ["ROLE_USER"];
    }
  };

  const userRoles = getUserRoles();

  // 2. Função validadora que verifica se o usuário possui permissão para ver o item
  const hasAccess = (item: NavItem): boolean => {
    if (!item.roles) return true; // Se o item não exigir roles, é público
    return item.roles.some(role => userRoles.includes(role));
  };

  const toggleItem = (title: string) => {
    setOpenItems(prev =>
      prev.includes(title)
        ? prev.filter(item => item !== title)
        : [...prev, title]
    );
  };

  const renderNavItem = (item: NavItem, level = 0) => {
    // Se o perfil atual não possuir permissão de acesso, aborta a renderização do nó imediatamente
    if (!hasAccess(item)) return null;

    const hasChildren = item.children && item.children.length > 0;
    const isOpen = openItems.includes(item.title);
    const isSelected = item.href ? location.pathname === item.href : false;

    if (hasChildren) {
      return (
        <Collapsible key={item.title} open={isOpen} onOpenChange={() => toggleItem(item.title)}>
          <CollapsibleTrigger asChild>
            <Button
              variant="ghost"
              className={cn(
                "w-full justify-start gap-3 font-normal transition-all duration-200",
                level > 0 && "pl-6",
                "hover:bg-slate-100 text-slate-700 hover:text-slate-900"
              )}
            >
              <item.icon className="h-4 w-4 text-slate-500" />
              <span className="flex-1 text-left">{item.title}</span>
              {isOpen ? (
                <ChevronDown className="h-4 w-4 text-slate-400" />
              ) : (
                <ChevronRight className="h-4 w-4 text-slate-400" />
              )}
            </Button>
          </CollapsibleTrigger>
          <CollapsibleContent className="space-y-1 mt-1">
            {item.children?.map(child => renderNavItem(child, level + 1))}
          </CollapsibleContent>
        </Collapsible>
      );
    }

    return (
      <Button
        key={item.title}
        variant="ghost"
        asChild // Crucial para permitir que o Botão herde as propriedades semânticas do componente Link abaixo
        className={cn(
          "w-full justify-start gap-3 font-normal transition-all duration-200",
          level > 0 && "pl-8",
          isSelected 
            ? "bg-indigo-50 text-indigo-700 font-medium hover:bg-indigo-100/80" 
            : "hover:bg-slate-100 text-slate-600 hover:text-slate-900"
        )}
      >
        <Link to={item.href || "#"}>
          <item.icon className={cn("h-4 w-4", isSelected ? "text-indigo-600" : "text-slate-400")} />
          <span className="flex-1 text-left">{item.title}</span>
          {item.badge && (
            <span className={cn(
              "text-[10px] font-bold px-2 py-0.5 rounded-full ml-auto",
              isSelected ? "bg-indigo-600 text-white" : "bg-slate-200 text-slate-700"
            )}>
              {item.badge}
            </span>
          )}
        </Link>
      </Button>
    );
  };

  return (
    <div className={cn("pb-12 border-slate-100", className)}>
      <div className="space-y-4 py-4">
        <div className="px-3 py-2">
          <div className="space-y-1">
            <h2 className="mb-2 px-4 text-xs font-semibold uppercase tracking-wider text-slate-400">
              Navegação
            </h2>
            <div className="space-y-1">
              {navigationItems.map(item => renderNavItem(item))}
            </div>
          </div>
        </div>
        
        <div className="px-3 py-2">
          <div className="space-y-1">
            <h2 className="mb-2 px-4 text-xs font-semibold uppercase tracking-wider text-slate-400">
              Sistema
            </h2>
            <Button
              variant="ghost"
              asChild
              className={cn(
                "w-full justify-start gap-3 font-normal text-slate-600 hover:text-slate-900 hover:bg-slate-100 transition-all duration-200",
                location.pathname === "/configuracoes" && "bg-indigo-50 text-indigo-700"
              )}
            >
              <Link to="/configuracoes">
                <Settings className="h-4 w-4 text-slate-400" />
                <span>Configurações</span>
              </Link>
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
}