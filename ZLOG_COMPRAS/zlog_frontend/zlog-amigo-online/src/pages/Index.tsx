import { 
  FileText, 
  ShoppingCart, 
  Users, 
  Package, 
  TrendingUp, 
  AlertCircle,
  CheckCircle,
  Clock
} from "lucide-react";
import { Layout } from "@/components/layout/Layout";
import { StatsCard } from "@/components/dashboard/StatsCard";
import { QuickActions } from "@/components/dashboard/QuickActions";
import { RecentActivity } from "@/components/dashboard/RecentActivity";
import { DashboardChart } from "@/components/dashboard/DashboardChart";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import heroImage from "@/assets/hero-image.jpg";

const Index = () => {
  return (
    <Layout>
      {/* Hero Section */}
      <div className="relative rounded-lg overflow-hidden mb-8 h-48 bg-gradient-hero">
        <div 
          className="absolute inset-0 bg-cover bg-center bg-no-repeat opacity-20"
          style={{ backgroundImage: `url(${heroImage})` }}
        />
        <div className="relative z-10 h-full flex items-center justify-between p-8">
          <div>
            <h1 className="text-3xl font-bold text-white mb-2">
              Bem-vindo ao ZLOG Compras
            </h1>
            <p className="text-white/90 text-lg">
              Sistema inteligente de gestão de compras empresariais
            </p>
          </div>
          <Button variant="secondary" size="lg" className="bg-white/20 backdrop-blur-sm text-white border-white/30 hover:bg-white/30">
            Criar Nova Solicitação
          </Button>
        </div>
      </div>

      {/* Stats Cards */}
      <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-4 mb-8">
        <StatsCard
          title="Solicitações Ativas"
          value="156"
          description="24 aguardando aprovação"
          icon={<FileText className="h-4 w-4" />}
          trend={{ value: 12, isPositive: true }}
        />
        <StatsCard
          title="Pedidos em Andamento"
          value="89"
          description="12 recebidos hoje"
          icon={<ShoppingCart className="h-4 w-4" />}
          trend={{ value: 8, isPositive: true }}
        />
        <StatsCard
          title="Fornecedores Ativos"
          value="45"
          description="3 novos este mês"
          icon={<Users className="h-4 w-4" />}
          trend={{ value: 5, isPositive: true }}
        />
        <StatsCard
          title="Economia Total"
          value="R$ 125.850"
          description="No último trimestre"
          icon={<TrendingUp className="h-4 w-4" />}
          trend={{ value: 18, isPositive: true }}
        />
      </div>

      {/* Main Content Grid */}
      <div className="grid gap-6 lg:grid-cols-3">
        {/* Left Column - Charts */}
        <div className="lg:col-span-2 space-y-6">
          <DashboardChart />
          
          {/* Resumo de Aprovações */}
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <AlertCircle className="h-5 w-5 text-warning" />
                Pendências de Aprovação
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="grid gap-4 md:grid-cols-3">
                <div className="flex items-center gap-3 p-4 rounded-lg border bg-warning/5">
                  <Clock className="h-8 w-8 text-warning" />
                  <div>
                    <div className="text-2xl font-bold">24</div>
                    <div className="text-sm text-muted-foreground">Aguardando</div>
                  </div>
                </div>
                <div className="flex items-center gap-3 p-4 rounded-lg border bg-success/5">
                  <CheckCircle className="h-8 w-8 text-success" />
                  <div>
                    <div className="text-2xl font-bold">132</div>
                    <div className="text-sm text-muted-foreground">Aprovadas</div>
                  </div>
                </div>
                <div className="flex items-center gap-3 p-4 rounded-lg border bg-destructive/5">
                  <AlertCircle className="h-8 w-8 text-destructive" />
                  <div>
                    <div className="text-2xl font-bold">8</div>
                    <div className="text-sm text-muted-foreground">Rejeitadas</div>
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>
        </div>

        {/* Right Column - Sidebar */}
        <div className="space-y-6">
          <QuickActions />
          <RecentActivity />
        </div>
      </div>
    </Layout>
  );
};

export default Index;
