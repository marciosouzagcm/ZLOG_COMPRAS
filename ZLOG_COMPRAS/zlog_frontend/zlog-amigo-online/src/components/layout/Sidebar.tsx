import {
  LayoutDashboard,
  ShoppingCart,
  FileText,
  Users,
  Package,
  TrendingUp,
  Settings,
  ChevronDown,
  ChevronRight,
} from "lucide-react";
import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from "@/components/ui/collapsible";
import { useState } from "react";

interface SidebarProps {
  className?: string;
}

interface NavItem {
  title: string;
  icon: any;
  href?: string;
  children?: NavItem[];
  badge?: string;
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
      { title: "Aguardando Aprovação", icon: FileText, href: "/aprovacoes", badge: "5" },
    ],
  },
  {
    title: "Cotações",
    icon: TrendingUp,
    children: [
      { title: "Orçamentos", icon: TrendingUp, href: "/orcamentos" },
      { title: "Comparativo", icon: TrendingUp, href: "/comparativo" },
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
  },
  {
    title: "Produtos",
    icon: Package,
    href: "/produtos",
  },
  {
    title: "Relatórios",
    icon: TrendingUp,
    children: [
      { title: "Analytics", icon: TrendingUp, href: "/relatorios/analytics" },
      { title: "Exportar Dados", icon: TrendingUp, href: "/relatorios/exportar" },
    ],
  },
];

export function Sidebar({ className }: SidebarProps) {
  const [openItems, setOpenItems] = useState<string[]>(["Solicitações"]);

  const toggleItem = (title: string) => {
    setOpenItems(prev =>
      prev.includes(title)
        ? prev.filter(item => item !== title)
        : [...prev, title]
    );
  };

  const renderNavItem = (item: NavItem, level = 0) => {
    const hasChildren = item.children && item.children.length > 0;
    const isOpen = openItems.includes(item.title);

    if (hasChildren) {
      return (
        <Collapsible key={item.title} open={isOpen} onOpenChange={() => toggleItem(item.title)}>
          <CollapsibleTrigger asChild>
            <Button
              variant="ghost"
              className={cn(
                "w-full justify-start gap-3 font-normal transition-smooth",
                level > 0 && "pl-6",
                "hover:bg-sidebar-accent hover:text-sidebar-accent-foreground"
              )}
            >
              <item.icon className="h-4 w-4" />
              <span className="flex-1 text-left">{item.title}</span>
              {isOpen ? (
                <ChevronDown className="h-4 w-4" />
              ) : (
                <ChevronRight className="h-4 w-4" />
              )}
            </Button>
          </CollapsibleTrigger>
          <CollapsibleContent className="space-y-1">
            {item.children?.map(child => renderNavItem(child, level + 1))}
          </CollapsibleContent>
        </Collapsible>
      );
    }

    return (
      <Button
        key={item.title}
        variant="ghost"
        className={cn(
          "w-full justify-start gap-3 font-normal transition-smooth",
          level > 0 && "pl-6",
          "hover:bg-sidebar-accent hover:text-sidebar-accent-foreground"
        )}
      >
        <item.icon className="h-4 w-4" />
        <span className="flex-1 text-left">{item.title}</span>
        {item.badge && (
          <span className="bg-primary text-primary-foreground text-xs px-2 py-1 rounded-full">
            {item.badge}
          </span>
        )}
      </Button>
    );
  };

  return (
    <div className={cn("pb-12", className)}>
      <div className="space-y-4 py-4">
        <div className="px-3 py-2">
          <div className="space-y-1">
            <h2 className="mb-2 px-4 text-lg font-semibold tracking-tight text-sidebar-foreground">
              Navegação
            </h2>
            <div className="space-y-1">
              {navigationItems.map(item => renderNavItem(item))}
            </div>
          </div>
        </div>
        
        <div className="px-3 py-2">
          <div className="space-y-1">
            <h2 className="mb-2 px-4 text-lg font-semibold tracking-tight text-sidebar-foreground">
              Sistema
            </h2>
            <Button
              variant="ghost"
              className="w-full justify-start gap-3 font-normal hover:bg-sidebar-accent hover:text-sidebar-accent-foreground transition-smooth"
            >
              <Settings className="h-4 w-4" />
              <span>Configurações</span>
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
}