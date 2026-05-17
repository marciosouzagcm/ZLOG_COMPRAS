import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import api from "@/services/api";
import { TrendingUp } from "lucide-react";
import { useEffect, useState } from "react";
import { Bar, BarChart, CartesianGrid, Cell, Pie, PieChart, ResponsiveContainer, Tooltip, XAxis, YAxis } from "recharts";

interface MonthlyData { mes: string; solicitacoes: number; pedidos: number; }
interface StatusData { name: string; value: number; color: string; }

export function DashboardChart() {
  const [monthlyData, setMonthlyData] = useState<MonthlyData[]>([]);
  const [statusData, setStatusData] = useState<StatusData[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null); // Guardar a mensagem de erro real

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        setLoading(true);
        console.log("Tentando buscar dados do dashboard...");
        
        const [monthlyResponse, statusResponse] = await Promise.all([
          api.get<MonthlyData[]>("/dashboard/monthly-data"),
          api.get<StatusData[]>("/dashboard/status-data")
        ]);

        console.log("Dados recebidos com sucesso:", monthlyResponse.data);
        setMonthlyData(monthlyResponse.data);
        setStatusData(statusResponse.data);
        setError(null);
      } catch (err: any) {
        console.error("Erro detalhado:", err);
        setError(err.response?.status === 403 ? "Acesso Negado (403). Verifique o Token." : "Erro de conexão.");
      } finally {
        setLoading(false);
      }
    };

    fetchDashboardData();
  }, []);

  if (loading) return <div className="flex justify-center items-center h-[50vh]"><p>Carregando...</p></div>;

  if (error) {
    return (
      <div className="flex flex-col justify-center items-center h-[50vh] text-red-500">
        <p>{error}</p>
        <button onClick={() => window.location.reload()} className="mt-4 px-4 py-2 bg-slate-200 rounded">Recarregar</button>
      </div>
    );
  }

  return (
    <div className="grid gap-6 md:grid-cols-2">
      {/* Gráfico de Barras */}
      <Card>
        <CardHeader><CardTitle className="flex items-center gap-2"><TrendingUp /> Solicitações vs Pedidos</CardTitle></CardHeader>
        <CardContent>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={monthlyData}>
              <CartesianGrid strokeDasharray="3 3" vertical={false} />
              <XAxis dataKey="mes" />
              <YAxis />
              <Tooltip />
              <Bar dataKey="solicitacoes" fill="#3b82f6" name="Solicitações" radius={[4, 4, 0, 0]} />
              <Bar dataKey="pedidos" fill="#10b981" name="Pedidos" radius={[4, 4, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </CardContent>
      </Card>

      {/* Gráfico de Pizza */}
      <Card>
        <CardHeader><CardTitle className="flex items-center gap-2"><TrendingUp /> Status</CardTitle></CardHeader>
        <CardContent>
          <ResponsiveContainer width="100%" height={300}>
            <PieChart>
              <Pie data={statusData} dataKey="value" nameKey="name" cx="50%" cy="50%" outerRadius={80} label>
                {statusData.map((entry, index) => <Cell key={`cell-${index}`} fill={entry.color} />)}
              </Pie>
              <Tooltip />
            </PieChart>
          </ResponsiveContainer>
        </CardContent>
      </Card>
    </div>
  );
}