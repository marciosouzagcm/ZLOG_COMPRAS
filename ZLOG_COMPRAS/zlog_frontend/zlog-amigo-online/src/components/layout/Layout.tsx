import { ReactNode } from "react";
import { Header } from "./Header";
import { Sidebar } from "./Sidebar";
import { cn } from "@/lib/utils";

interface LayoutProps {
  children: ReactNode;
  className?: string;
}

export function Layout({ children, className }: LayoutProps) {
  return (
    <div className="min-h-screen bg-background">
      <Header />
      <div className="flex">
        <aside className="fixed left-0 top-16 z-40 w-64 h-[calc(100vh-4rem)] border-r bg-sidebar overflow-y-auto">
          <Sidebar />
        </aside>
        <main className={cn("flex-1 ml-64", className)}>
          <div className="container py-6 px-6">
            {children}
          </div>
        </main>
      </div>
    </div>
  );
}