package com.zlogcompras.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @GetMapping("/monthly-data")
    public ResponseEntity<List<MonthlyData>> getMonthlyData() {
        List<MonthlyData> data = Arrays.asList(
            new MonthlyData("Jan", 24, 18),
            new MonthlyData("Fev", 32, 28),
            new MonthlyData("Mar", 41, 35),
            new MonthlyData("Abr", 38, 32),
            new MonthlyData("Mai", 45, 40),
            new MonthlyData("Jun", 52, 47)
        );
        return ResponseEntity.ok(data);
    }

    @GetMapping("/status-data")
    public ResponseEntity<List<StatusData>> getStatusData() {
        List<StatusData> data = Arrays.asList(
            new StatusData("Aprovadas", 65, "#22c55e"),
            new StatusData("Pendentes", 25, "#f59e0b"),
            new StatusData("Rejeitadas", 10, "#ef4444")
        );
        return ResponseEntity.ok(data);
    }

    // Classes internas para representar os dados do dashboard.
    // Em um projeto real, você usaria DTOs (Data Transfer Objects) separados.
    static class MonthlyData {
        public String mes;
        public int solicitacoes;
        public int pedidos;

        public MonthlyData(String mes, int solicitacoes, int pedidos) {
            this.mes = mes;
            this.solicitacoes = solicitacoes;
            this.pedidos = pedidos;
        }
    }

    static class StatusData {
        public String name;
        public int value;
        public String color;

        public StatusData(String name, int value, String color) {
            this.name = name;
            this.value = value;
            this.color = color;
        }
    }
}