import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { AlertCircle, CheckCircle, XCircle, Calendar, DollarSign } from "lucide-react";
import { useQuery } from "@tanstack/react-query";
import api from "@/services/api";

interface PendingApproval {
  id: string;
  title: string;
  description: string;
  value: string;
  requestedBy: string;
  date: string;
  priority: "alta" | "media" | "baixa";
}

const priorityConfig = {
  alta: { label: "Alta", variant: "destructive" as const },
  media: { label: "Média", variant: "secondary" as const },
  baixa: { label: "Baixa", variant: "outline" as const },
};

export function PendingApprovals() {
  const { data, isLoading, isError } = useQuery<PendingApproval[]>({
    queryKey: ['pendingApprovals'],
    queryFn: async () => {
      const response = await api.get<PendingApproval[]>("/dashboard/pending-approvals");
      return response.data as PendingApproval[];
    }
  });

  if (isLoading) {
    return (
      <Card>
        <CardHeader>
          <CardTitle>Aprovações Pendentes</CardTitle>
        </CardHeader>
        <CardContent>
          <p>Carregando dados...</p>
        </CardContent>
      </Card>
    );
  }

  if (isError) {
    return (
      <Card>
        <CardHeader>
          <CardTitle>Aprovações Pendentes</CardTitle>
        </CardHeader>
        <CardContent>
          <p className="text-red-500">Erro ao carregar os dados. Tente novamente mais tarde.</p>
        </CardContent>
      </Card>
    );
  }

  return (
    <Card>
      <CardHeader>
        <CardTitle className="flex items-center gap-2">
          <AlertCircle className="h-5 w-5 text-warning" />
          Aprovações Pendentes
        </CardTitle>
      </CardHeader>
      <CardContent className="space-y-4">
        {data?.map((approval) => (
          <div
            key={approval.id}
            className="p-4 rounded-lg border bg-card hover:bg-accent/50 transition-smooth"
          >
            <div className="flex items-start justify-between mb-3">
              <div>
                <h4 className="font-medium text-sm">{approval.title}</h4>
                <p className="text-sm text-muted-foreground">
                  {approval.description}
                </p>
              </div>
              <Badge variant={priorityConfig[approval.priority].variant}>
                {priorityConfig[approval.priority].label}
              </Badge>
            </div>
            <div className="flex items-center justify-between text-xs text-muted-foreground mb-3">
              <div className="flex items-center gap-4">
                <span className="flex items-center gap-1">
                  <DollarSign className="h-3 w-3" />
                  {approval.value}
                </span>
                <span className="flex items-center gap-1">
                  <Calendar className="h-3 w-3" />
                  {approval.date}
                </span>
              </div>
              <span>Por: {approval.requestedBy}</span>
            </div>
            <div className="flex gap-2">
              <Button size="sm" variant="success" className="flex-1">
                <CheckCircle className="h-3 w-3 mr-1" />
                Aprovar
              </Button>
              <Button size="sm" variant="outline" className="flex-1">
                <XCircle className="h-3 w-3 mr-1" />
                Rejeitar
              </Button>
            </div>
          </div>
        ))}
        {/* Lógica para mostrar o botão "Ver tudo" somente se houver dados */}
        {Array.isArray(data) && data.length > 0 && (
          <div className="text-center pt-2">
            <Button variant="ghost" size="sm">
              Ver todas as aprovações
            </Button>
          </div>
        )}
      </CardContent>
    </Card>
  );
}