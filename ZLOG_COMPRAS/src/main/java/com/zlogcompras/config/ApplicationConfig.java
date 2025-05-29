package com.zlogcompras.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zlogcompras.model.Estoque;
import com.zlogcompras.model.dto.EstoqueRequestDTO;
import com.zlogcompras.model.dto.EstoqueResponseDTO; // Importe este DTO

@Configuration
public class ApplicationConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // Mapeamento EstoqueRequestDTO -> Estoque (já existente)
        modelMapper.createTypeMap(EstoqueRequestDTO.class, Estoque.class)
            .addMappings(mapper -> mapper.skip(Estoque::setVersion));

        // --- ADICIONE ESTE NOVO MAPEAMENTO PARA Estoque -> EstoqueResponseDTO ---
        modelMapper.createTypeMap(Estoque.class, EstoqueResponseDTO.class)
            .addMappings(mapper -> {
                // Mapeia produto.id para produtoId
                mapper.map(src -> src.getProduto().getId(), EstoqueResponseDTO::setProdutoId);
                // Mapeia produto.nome para nomeProduto
                mapper.map(src -> src.getProduto().getNome(), EstoqueResponseDTO::setNomeProduto);
                // Mapeia produto.codigoProduto para codigoProduto
                mapper.map(src -> src.getProduto().getCodigoProduto(), EstoqueResponseDTO::setCodigoProduto);
                
                // Mapeia 'quantidade' da entidade para 'quantidadeAtual' do DTO
                mapper.map(Estoque::getQuantidade, EstoqueResponseDTO::setQuantidadeAtual);
                // Mapeia 'dataUltimaEntrada' da entidade para 'dataEntrada' do DTO
                mapper.map(Estoque::getDataUltimaEntrada, EstoqueResponseDTO::setDataEntrada);
                
                // Ignorar campos que você não quer mapear ou que não existem
                // Ex: Se EstoqueResponseDTO tiver 'dataSaida' e não quiser mapear dataUltimaSaida da entidade para ele
                // mapper.skip(EstoqueResponseDTO::setDataSaida);
            });
        // --- FIM DO NOVO MAPEAMENTO ---

        return modelMapper;
    }
}