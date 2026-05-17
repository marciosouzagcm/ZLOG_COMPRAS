
# 📋 ZLOG COMPRAS — Documento Situacional do Projeto

> **Versão do Documento:** 1.0  
> **Data de Referência:** 19 de Fevereiro de 2026  
> **Repositório:** [github.com/marciosouzagcm/ZLOG_COMPRAS](https://github.com/marciosouzagcm/ZLOG_COMPRAS)  
> **Último Commit:** `32aa38e` — "feat: corrige segurança JWT, CORS e integração do dashboard" (Dez/2025)  
> **Licença:** MIT  

---

## 1. VISÃO GERAL DO PROJETO

### 1.1 Descrição
O **ZLOG Compras** é um sistema Full Stack desenvolvido para otimizar e gerenciar o fluxo completo de compras em ambientes empresariais. O projeto visa se tornar uma solução **SaaS (Software as a Service)** open-source para automatizar e trazer transparência a todas as etapas da aquisição — desde a criação de solicitações de materiais até a geração de pedidos de compra e o controle de recebimento.

### 1.2 Objetivo de Negócio
Criar uma plataforma multi-tenant que permita empresas de diferentes portes gerenciarem seus processos de compras com:
- Rastreabilidade completa
- Transparência operacional
- Controle desde a requisição até a entrega e prestação de contas

### 1.3 Stack Tecnológica Atual

| Camada | Tecnologia | Versão |
|--------|-----------|--------|
| **Backend** | Java + Spring Boot | Java 21 / Spring Boot 3.3.0 |
| **Banco de Dados** | MySQL | - |
| **Persistência** | Spring Data JPA / Hibernate | - |
| **Build** | Maven | - |
| **Documentação API** | Swagger UI / OpenAPI | - |
| **Frontend** | Angular (Standalone Components) | Última versão |
| **Linguagem Frontend** | TypeScript | - |
| **Controle de Versão** | Git / GitHub | - |

---

## 2. ANÁLISE DO ESTADO ATUAL

### 2.1 Funcionalidades Implementadas

#### ✅ Backend (API REST — Spring Boot)

| # | Funcionalidade | Status | Observações |
|---|---------------|--------|-------------|
| 1 | Gerenciamento de Solicitações de Compra | ✅ Implementado | CRUD completo |
| 2 | Processo de Cotação e Orçamento | ✅ Implementado | Criação, gestão e aprovação |
| 3 | Validação de Orçamentos (bloqueio para solicitações finalizadas) | ✅ Implementado | Regra de negócio crítica |
| 4 | Geração de Pedidos de Compra | ✅ Implementado | A partir de orçamentos aprovados |
| 5 | Validações de Dados (preços/quantidades) | ✅ Implementado | Consistência garantida |
| 6 | Atualização de Status do Pedido | ✅ Implementado | Controle de ciclo de vida |
| 7 | Endpoints de Recebimento de Itens | ⚠️ Estrutura Inicial | Apenas endpoints definidos |
| 8 | Segurança JWT | ✅ Implementado | Corrigido no último commit |
| 9 | Configuração CORS | ✅ Implementado | Integração frontend-backend |
| 10 | Dashboard (integração) | ⚠️ Em Progresso | Iniciado no último commit |

#### ✅ Frontend (Angular)

| # | Funcionalidade | Status | Observações |
|---|---------------|--------|-------------|
| 1 | Tela de Login | ⚠️ Parcial | Estrutura de componentes criada, sem integração real com API |
| 2 | Comunicação entre Componentes | ✅ Implementado | Via RxJS Observables |
| 3 | Dashboard | ⚠️ Em Progresso | Integração iniciada |

#### ✅ Testes

| Tipo | Quantidade | Cobertura | Status |
|------|-----------|-----------|--------|
| Unitários (Fornecedores) | Parte dos 18 | Módulo Fornecedores | ✅ 100% sucesso |
| Unitários (Orçamentos) | Parte dos 18 | Módulo Orçamentos | ✅ 100% sucesso |
| Integração | 0 | - | ❌ Não implementados |
| E2E | 0 | - | ❌ Não implementados |

---

## 3. MAPA DE MATURIDADE — COMPARATIVO COM MVP

### 3.1 Definição de MVP para um SaaS de Gestão de Compras

Um MVP (Minimum Viable Product) para um SaaS de compras empresariais deveria conter, no mínimo:

| Pilar | Funcionalidade | ZLOG Atual | Gap |
|-------|---------------|------------|-----|
| **Autenticação** | Login/Logout funcional com JWT | ⚠️ Parcial | Frontend não integrado |
| **Autenticação** | Registro de usuários | ❌ Ausente | Crítico |
| **Autenticação** | Recuperação de senha | ❌ Ausente | Importante |
| **Multi-tenancy** | Isolamento de dados por empresa | ❌ Ausente | **Crítico para SaaS** |
| **Autorização** | Controle de papéis (RBAC) | ❌ Ausente | Crítico |
| **Solicitações** | CRUD de Solicitações de Compra | ✅ Pronto | - |
| **Cotação** | Gestão de Orçamentos/Cotações | ✅ Pronto | - |
| **Pedidos** | Geração e gestão de Pedidos | ✅ Pronto | - |
| **Fornecedores** | Cadastro e gestão de Fornecedores | ✅ Pronto | - |
| **Recebimento** | Registro de recebimento de itens | ⚠️ Estrutura apenas | Funcionalidade incompleta |
| **Dashboard** | Visão geral com indicadores | ⚠️ Em progresso | Precisa ser finalizado |
| **Frontend** | Interface completa e funcional | ❌ Mínimo | **Gap crítico** |
| **Notificações** | Alertas de status/aprovações | ❌ Ausente | Importante |
| **Relatórios** | Exportação básica (PDF/Excel) | ❌ Ausente | Importante |
| **Deploy** | Ambiente de produção configurado | ❌ Ausente | Crítico para SaaS |
| **Billing** | Planos e cobrança | ❌ Ausente | Necessário para SaaS |

### 3.2 Nível de Completude do MVP

```
██████████░░░░░░░░░░░░░░░░░░░░  ~30-35%
```

**Classificação atual: PRÉ-MVP / Protótipo Funcional (Backend)**

O projeto encontra-se em estágio de **protótipo funcional do backend**. As regras de negócio centrais (solicitação → cotação → pedido) estão implementadas e testadas, porém faltam componentes essenciais para ser considerado um MVP viável para uso real.

---

## 4. ANÁLISE SWOT DO PROJETO

### Forças (Strengths)
- ✅ Regras de negócio centrais já implementadas e testadas
- ✅ Stack moderna e escalável (Java 21, Spring Boot 3.3)
- ✅ Documentação da API via Swagger
- ✅ Testes unitários com 100% de sucesso nos módulos cobertos
- ✅ Segurança JWT já implementada
- ✅ Projeto open-source (Licença MIT)
- ✅ Código organizado em estrutura clara backend/frontend

### Fraquezas (Weaknesses)
- ❌ Frontend extremamente incipiente (apenas tela de login simulada)
- ❌ Sem multi-tenancy (impedimento para SaaS)
- ❌ Sem controle de autorização por papéis (RBAC)
- ❌ Cobertura de testes limitada (apenas 18 testes unitários)
- ❌ Sem testes de integração ou E2E
- ❌ Nenhum ambiente de deploy configurado
- ❌ Arquivos desnecessários no repositório (hs_err_pid, replay_pid, logs)
- ❌ Linguagem do GitHub: 100% Java (Angular não detectado — possível problema de estrutura)

### Oportunidades (Opportunities)
- 🔵 Mercado brasileiro carente de SaaS de compras acessível
- 🔵 Modelo open-source atrai contribuidores e validação comunitária
- 🔵 Base sólida de backend para acelerar desenvolvimento do frontend
- 🔵 Possibilidade de pivotear o frontend para React (maior ecossistema)

### Ameaças (Threats)
- 🔴 Projetos similares mais maduros no mercado (Compras.gov, Merx, etc.)
- 🔴 Risco de abandono por ser projeto individual (1 contribuidor)
- 🔴 Tempo estimado para MVP completo pode desmotivar

---

## 5. ROADMAP SUGERIDO PARA MVP

### Fase 1 — Fundação (Estimativa: 4-6 semanas)
1. **Higienização do Repositório**
   - Remover arquivos de log e crash dumps
   - Configurar .gitignore adequadamente
   - Padronizar estrutura de pastas
2. **Multi-tenancy**
   - Implementar isolamento de dados por tenant (empresa)
   - Adicionar campo `tenant_id` em todas as entidades
3. **Autenticação Completa**
   - Integrar login real frontend ↔ backend (JWT)
   - Implementar registro de usuários
   - Implementar recuperação de senha
4. **Autorização (RBAC)**
   - Definir papéis: ADMIN, COMPRADOR, APROVADOR, SOLICITANTE
   - Implementar controle de acesso por endpoint

### Fase 2 — Frontend Funcional (Estimativa: 6-8 semanas)
1. **Módulo de Solicitações** (listar, criar, editar, aprovar)
2. **Módulo de Cotações/Orçamentos** (listar, comparar, aprovar)
3. **Módulo de Pedidos de Compra** (listar, acompanhar status)
4. **Módulo de Fornecedores** (CRUD completo)
5. **Dashboard com Indicadores** (KPIs, gráficos, alertas)
6. **Layout Responsivo** com navegação por menus

### Fase 3 — Produção e SaaS (Estimativa: 4-6 semanas)
1. **Deploy em nuvem** (AWS, GCP ou Azure)
2. **CI/CD Pipeline** (GitHub Actions)
3. **Monitoramento** (logs centralizados, health checks)
4. **Onboarding de Tenant** (registro de empresa)
5. **Billing** (integração com gateway de pagamento — Stripe, PagSeguro)
6. **Documentação de Usuário** (guia de uso, FAQ)

### Fase 4 — Pós-MVP (Evolução Contínua)
1. Notificações em tempo real (WebSocket)
2. Relatórios avançados (PDF/Excel)
3. Integração com ERPs
4. App mobile (PWA ou nativo)
5. Marketplace de fornecedores

---

## 6. INDICADORES E MÉTRICAS DO REPOSITÓRIO

| Métrica | Valor |
|---------|-------|
| Total de Commits | 40 |
| Branches | 2 (master, main) |
| Releases | 0 |
| Packages | 0 |
| Contribuidores | 1 |
| Stars | 0 |
| Forks | 0 |
| Watchers | 1 |
| Linguagens Detectadas | Java 100% |
| Último Commit | Dezembro 2025 |
| Idade do Projeto | ~8 meses (Jun 2025 – Fev 2026) |
| Testes Unitários | 18 (100% sucesso) |

---

## 7. RISCOS IDENTIFICADOS

| # | Risco | Severidade | Mitigação Sugerida |
|---|-------|-----------|-------------------|
| 1 | Projeto com apenas 1 contribuidor | 🔴 Alta | Buscar co-fundador técnico ou contribuidores open-source |
| 2 | Frontend quase inexistente | 🔴 Alta | Priorizar desenvolvimento frontend ou migrar para framework mais produtivo |
| 3 | Sem multi-tenancy | 🔴 Alta | Implementar antes de qualquer venda/onboarding |
| 4 | Sem ambiente de produção | 🟡 Média | Configurar CI/CD e deploy em cloud |
| 5 | Cobertura de testes baixa | 🟡 Média | Implementar testes de integração e E2E |
| 6 | Arquivos desnecessários no repo | 🟢 Baixa | Limpar repositório e atualizar .gitignore |
| 7 | 2 meses sem commits (Dez 2025 – Fev 2026) | 🟡 Média | Retomar desenvolvimento com cadência regular |
| 8 | Sem documentação de arquitetura | 🟡 Média | Criar diagrams C4 e documentação ADR |

---

## 8. RECOMENDAÇÕES ESTRATÉGICAS

### 8.1 Decisão Crítica: Frontend
O frontend Angular atual está em estágio muito inicial. Recomenda-se avaliar:
- **Opção A:** Continuar com Angular — manter consistência, porém curva de desenvolvimento mais lenta para um desenvolvedor solo.
- **Opção B:** Migrar para React — ecossistema maior, mais bibliotecas de componentes prontos (shadcn/ui, Material UI), maior pool de contribuidores.
- **Opção C:** Utilizar plataforma low-code (como Lovable) para acelerar o frontend — ideal para chegar ao MVP mais rápido.

### 8.2 Priorização
A prioridade absoluta para alcançar o MVP deve ser:
1. 🔴 **Multi-tenancy** (sem isso, não há SaaS)
2. 🔴 **Autenticação completa** (registro + login + recuperação)
3. 🔴 **Frontend funcional** (interface para as APIs existentes)
4. 🟡 **RBAC** (controle de acesso por papel)
5. 🟡 **Deploy** (ambiente de produção)

### 8.3 Quick Wins
- Limpar repositório (remover logs, crash dumps)
- Adicionar README badges (build status, coverage, license)
- Configurar GitHub Actions para CI básico
- Criar issues no GitHub para organizar backlog

---

## 9. CONCLUSÃO

O **ZLOG Compras** possui uma base sólida de backend com as regras de negócio centrais do fluxo de compras implementadas e testadas. No entanto, **o projeto está em aproximadamente 30-35% do caminho para um MVP viável como SaaS**.

Os principais gaps são:
1. **Frontend funcional** — a interface atual é apenas um protótipo de login
2. **Multi-tenancy** — requisito fundamental para qualquer SaaS
3. **Infraestrutura de produção** — sem deploy, CI/CD ou monitoramento

Com foco e priorização adequada, estimamos que o MVP poderia estar pronto em **14-20 semanas** de desenvolvimento dedicado, considerando um desenvolvedor full-time.

O projeto tem potencial significativo, especialmente no mercado brasileiro onde há carência de soluções SaaS acessíveis para gestão de compras empresariais.

---

> **Documento gerado como base estrutural do projeto ZLOG Compras.**  
> **Deve ser revisado e atualizado a cada sprint/ciclo de desenvolvimento.**
