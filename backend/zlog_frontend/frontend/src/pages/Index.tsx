import { 
  FileText, 
  ShoppingCart, 
  Users, 
  TrendingUp, 
  AlertCircle,
  CheckCircle,
  Clock
} from "lucide-react";
import { useQuery } from "@tanstack/react-query";
import api from "@/services/api";

// Componentes internos de Layout e Dashboard
import { Layout } from "@/components/layout/Layout";
import { StatsCard } from "@/components/dashboard/StatsCard";
import { QuickActions } from "@/components/dashboard/QuickActions";
import { RecentActivity } from "@/components/dashboard/RecentActivity";
import { DashboardChart } from "@/components/dashboard/DashboardChart";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";

// 1. Contrato TypeScript que espelha exatamente o DTO gerencial do seu Backend Java
interface DashboardStats {
  solicitacoesAtivas: number;
  solicitacoesAguardando: number;
  pedidosAndamento: number;
  pedidosRecebidosHoje: number;
  fornecedoresAtivos: number;
  fornecedoresNovosMes: number;
  economiaTrimestre: number;
  aprovacoesAguardando: number;
  aprovacoesAprovadas: number;
  aprovacoesRejeitadas: number;
}

const Index = () => {
  // 2. Integração com TanStack Query buscando dados reais de um endpoint do Spring Boot (Ex: /api/dashboard/stats)
  const { data: stats, isLoading, isError } = useQuery<DashboardStats>({
    queryKey: ["dashboard-stats"],
    queryFn: async () => {
      const response = await api.get<DashboardStats>("/dashboard/stats");
      return response.data;
    },
    // Mantém dados antigos em cache enquanto busca novos e previne re-renderização agressiva
    staleTime: 1000 * 60 * 5, // 5 minutos
  });

  // Valores de Fallback (Mock) caso a API ainda esteja sendo configurada ou em carregamento
  const displayStats = stats || {
    solicitacoesAtivas: 156,
    solicitacoesAguardando: 24,
    pedidosAndamento: 89,
    pedidosRecebidosHoje: 12,
    fornecedoresAtivos: 45,
    fornecedoresNovosMes: 3,
    economiaTrimestre: 125850,
    aprovacoesAguardando: 24,
    aprovacoesAprovadas: 132,
    aprovacoesRejeitadas: 8
  };

  // Formatador de moeda brasileiro para a economia real calculada
  const formatarMoeda = (valor: number) => {
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(valor);
  };

  return (
    <Layout>
      {/* Hero Section - Otimizada sem dependência de asset binário rígido */}
      <div className="relative rounded-lg overflow-hidden mb-8 h-48 bg-gradient-to-r from-slate-900 via-indigo-950 to-slate-900 shadow-sm">
        <div className="absolute inset-0 bg-grid-white/[0.02] bg-[size:20px_20px]" />
        <div className="relative z-10 h-full flex items-center justify-between p-8">
          <div>
            <h1 className="text-3xl font-bold text-white mb-2">
              Bem-vindo ao ZLOG Compras
            </h1>
            <p className="text-indigo-200/90 text-lg">
              Sistema inteligente de gestão de compras empresariais
            </p>
          </div>
          <Button 
            variant="secondary" 
            size="lg" 
            className="bg-white/10 backdrop-blur-md text-white border border-white/20 hover:bg-white/20 transition-all"
          >
            Criar Nova Solicitação
          </Button>
        </div>
      </div>

      {/* Grid de Cartões de Métricas */}
      <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-4 mb-8">
        <StatsCard
          title="Solicitações Ativas"
          value={isLoading ? "..." : String(displayStats.solicitacoesAtivas)}
          description={`${displayStats.solicitacoesAguardando} aguardando aprovação`}
          icon={<FileText className="h-4 w-4 text-indigo-500" />}
          trend={{ value: 12, isPositive: true }}
        />
        <StatsCard
          title="Pedidos em Andamento"
          value={isLoading ? "..." : String(displayStats.pedidosAndamento)}
          description={`${displayStats.pedidosRecebidosHoje} recebidos hoje`}
          icon={<ShoppingCart className="h-4 w-4 text-emerald-500" />}
          trend={{ value: 8, isPositive: true }}
        />
        <StatsCard
          title="Fornecedores Ativos"
          value={isLoading ? "..." : String(displayStats.fornecedoresAtivos)}
          description={`${displayStats.fornecedoresNovosMes} novos este mês`}
          icon={<Users className="h-4 w-4 text-blue-500" />}
          trend={{ value: 5, isPositive: true }}
        />
        <StatsCard
          title="Economia Total"
          value={isLoading ? "..." : formatarMoeda(displayStats.economiaTrimestre)}
          description="No último trimestre"
          icon={<TrendingUp className="h-4 w-4 text-amber-500" />}
          trend={{ value: 18, isPositive: true }}
        />
      </div>

      {/* Main Content Grid */}
      <div className="grid gap-6 lg:grid-cols-3">
        {/* Coluna da Esquerda: Gráficos e Tabelas */}
        <div className="lg:col-span-2 space-y-6">
          <DashboardChart />
          
          {/* Bloco Resumo de Aprovações usando cores semânticas padrão do Tailwind */}
          <Card className="shadow-sm border-slate-200">
            <CardHeader>
              <CardTitle className="text-lg font-semibold flex items-center gap-2 text-slate-800">
                <AlertCircle className="h-5 w-5 text-amber-500" />
                Pendências de Aprovação
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="grid gap-4 md:grid-cols-3">
                
                <div className="flex items-center gap-3 p-4 rounded-lg border border-amber-100 bg-amber-50/50">
                  <Clock className="h-8 w-8 text-amber-500" />
                  <div>
                    <div className="text-2xl font-bold text-amber-700">{displayStats.aprovacoesAguardando}</div>
                    <div className="text-xs font-medium text-amber-600 uppercase tracking-wider">Aguardando</div>
                  </div>
                </div>

                <div className="flex items-center gap-3 p-4 rounded-lg border border-emerald-100 bg-emerald-50/50">
                  <CheckCircle className="h-8 w-8 text-emerald-500" />
                  <div>
                    <div className="text-2xl font-bold text-emerald-700">{displayStats.aprovacoesAprovadas}</div>
                    <div className="text-xs font-medium text-emerald-600 uppercase tracking-wider">Aprovadas</div>
                  </div>
                </div>

                <div className="flex items-center gap-3 p-4 rounded-lg border border-red-100 bg-red-50/50">
                  <AlertCircle className="h-8 w-8 text-red-500" />
                  <div>
                    <div className="text-2xl font-bold text-red-700">{displayStats.aprovacoesRejeitadas}</div>
                    <div className="text-xs font-medium text-red-600 uppercase tracking-wider">Rejeitadas</div>
                  </div>
                </div>

              </div>
              {isError && (
                <p className="text-xs text-red-500 mt-3 text-center">
                  Não foi possível sincronizar os dados em tempo real com o servidor. Exibindo dados locais cacheáveis.
                </p>
              )}
            </CardContent>
          </Card>
        </div>

        {/* Coluna da Direita: Sidebar Operacional */}
        <div className="space-y-6">
          <QuickActions />
          <RecentActivity />
        </div>
      </div>
    </Layout>
  );
};

export default Index;