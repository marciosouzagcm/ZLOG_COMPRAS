import { Plus, FileText, ShoppingCart, Users, Package } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";

interface QuickAction {
  title: string;
  description: string;
  icon: any;
  variant: "primary" | "secondary" | "outline" | "default";
}

const quickActions: QuickAction[] = [
  {
    title: "Nova Solicitação",
    description: "Criar nova solicitação de compra",
    icon: Plus,
    variant: "primary",
  },
  {
    title: "Revisar Orçamentos",
    description: "Avaliar propostas pendentes",
    icon: FileText,
    variant: "secondary",
  },
  {
    title: "Gerar Pedido",
    description: "Criar pedido de compra",
    icon: ShoppingCart,
    variant: "outline",
  },
  {
    title: "Cadastrar Fornecedor",
    description: "Adicionar novo fornecedor",
    icon: Users,
    variant: "outline",
  },
];

export function QuickActions() {
  return (
    <Card>
      <CardHeader>
        <CardTitle className="flex items-center gap-2">
          <Package className="h-5 w-5 text-primary" />
          Ações Rápidas
        </CardTitle>
      </CardHeader>
      <CardContent className="space-y-3">
        {quickActions.map((action, index) => (
          <Button
            key={index}
            variant={action.variant}
            className="w-full justify-start gap-3 h-auto p-4 transition-smooth hover:shadow-card"
          >
            <div className="flex items-center justify-center h-10 w-10 rounded-lg bg-primary/10 text-primary">
              <action.icon className="h-5 w-5" />
            </div>
            <div className="text-left">
              <div className="font-medium">{action.title}</div>
              <div className="text-sm text-muted-foreground">
                {action.description}
              </div>
            </div>
          </Button>
        ))}
      </CardContent>
    </Card>
  );
}