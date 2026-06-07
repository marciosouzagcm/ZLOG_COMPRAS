import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Clock, FileText, ShoppingCart, CheckCircle, AlertCircle } from "lucide-react";
import { cn } from "@/lib/utils";

interface Activity {
  id: string;
  type: "solicitacao" | "orcamento" | "pedido" | "aprovacao";
  title: string;
  description: string;
  time: string;
  status: "pendente" | "aprovado" | "rejeitado" | "concluido";
}

const recentActivities: Activity[] = [
  {
    id: "1",
    type: "solicitacao",
    title: "Solicitação #2024-001",
    description: "Material de escritório - Papelaria",
    time: "há 2 horas",
    status: "pendente",
  },
  {
    id: "2",
    type: "orcamento",
    title: "Orçamento #ORÇ-001",
    description: "Fornecedor XYZ - R$ 2.500,00",
    time: "há 4 horas",
    status: "aprovado",
  },
  {
    id: "3",
    type: "pedido",
    title: "Pedido #PED-001",
    description: "Equipamentos de TI",
    time: "há 1 dia",
    status: "concluido",
  },
  {
    id: "4",
    type: "aprovacao",
    title: "Aprovação Pendente",
    description: "Solicitação aguardando aprovação",
    time: "há 2 dias",
    status: "pendente",
  },
];

const statusConfig = {
  pendente: {
    label: "Pendente",
    variant: "secondary" as const,
    icon: Clock,
    color: "text-warning",
  },
  aprovado: {
    label: "Aprovado",
    variant: "secondary" as const,
    icon: CheckCircle,
    color: "text-success",
  },
  rejeitado: {
    label: "Rejeitado",
    variant: "destructive" as const,
    icon: AlertCircle,
    color: "text-destructive",
  },
  concluido: {
    label: "Concluído",
    variant: "secondary" as const,
    icon: CheckCircle,
    color: "text-success",
  },
};

const typeIcons = {
  solicitacao: FileText,
  orcamento: FileText,
  pedido: ShoppingCart,
  aprovacao: AlertCircle,
};

export function RecentActivity() {
  return (
    <Card>
      <CardHeader>
        <CardTitle className="flex items-center gap-2">
          <Clock className="h-5 w-5 text-primary" />
          Atividade Recente
        </CardTitle>
      </CardHeader>
      <CardContent className="space-y-4">
        {recentActivities.map((activity) => {
          const StatusIcon = statusConfig[activity.status].icon;
          const TypeIcon = typeIcons[activity.type];
          
          return (
            <div
              key={activity.id}
              className="flex items-start gap-3 p-3 rounded-lg border bg-card hover:bg-accent/50 transition-smooth cursor-pointer"
            >
              <div className="flex items-center justify-center h-10 w-10 rounded-lg bg-primary/10 text-primary">
                <TypeIcon className="h-5 w-5" />
              </div>
              
              <div className="flex-1 space-y-1">
                <div className="flex items-center justify-between">
                  <h4 className="text-sm font-medium">{activity.title}</h4>
                  <span className="text-xs text-muted-foreground">
                    {activity.time}
                  </span>
                </div>
                <p className="text-sm text-muted-foreground">
                  {activity.description}
                </p>
                <div className="flex items-center gap-2">
                  <StatusIcon className={cn("h-4 w-4", statusConfig[activity.status].color)} />
                  <Badge variant={statusConfig[activity.status].variant}>
                    {statusConfig[activity.status].label}
                  </Badge>
                </div>
              </div>
            </div>
          );
        })}
        
        <div className="text-center pt-2">
          <button className="text-sm text-primary hover:underline">
            Ver todas as atividades
          </button>
        </div>
      </CardContent>
    </Card>
  );
}