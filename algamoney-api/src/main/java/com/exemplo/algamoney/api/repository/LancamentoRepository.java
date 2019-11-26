package com.exemplo.algamoney.api.repository;

import com.exemplo.algamoney.api.model.Lancamento;
import com.exemplo.algamoney.api.repository.implementacaoLancamento.LancamentoRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery {
}
