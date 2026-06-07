package com.zlogcompras.model.dto;

import java.time.LocalDate;
import java.util.List;

import com.zlogcompras.model.StatusOrcamento; // Importar StatusOrcamento

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder; // Adicione o Builder se estiver usando-o para instanciar
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Adicione se estiver usando o padrão de projeto Builder
public class OrcamentoInputDTO {

    @NotNull(message = "O ID da Solicitação de Compra é obrigatório.")
    private Long solicitacaoCompraId;

    @NotNull(message = "O ID do Fornecedor é obrigatório.")
    private Long fornecedorId;

    private LocalDate dataCotacao;

    // Remova valorTotal, ele deve ser calculado no serviço e não vir do input
    // private BigDecimal valorTotal;

    private String numeroOrcamento;

    @Valid
    @NotEmpty(message = "O orçamento deve conter pelo menos um item.")
    private List<ItemOrcamentoInputDTO> itensOrcamento;

    // Use o tipo Enum StatusOrcamento se o mapper e a entidade esperam isso
    private StatusOrcamento status; // <<< Tipo ajustado para StatusOrcamento

    // Adicione esses campos se eles são esperados na criação/atualização do
    // orçamento
    // e você quer que o MapStruct os mapeie.
    // Caso contrário, as warnings do OrcamentoMapper persistirão para eles.
    private String observacoesGerais;
    private String condicoesPagamento;
    private String prazoEntrega;
}