package com.zlogcompras.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.zlogcompras.model.MovimentacaoEstoque;

@Repository
public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long> {

    // CORRIGIDO: Navega em 'produto' e busca pelo atributo 'codigo' dele
    List<MovimentacaoEstoque> findByProduto_CodigoOrderByDataMovimentacaoDesc(String codigo);
}