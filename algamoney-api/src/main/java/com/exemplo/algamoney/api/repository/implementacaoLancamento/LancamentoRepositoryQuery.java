package com.exemplo.algamoney.api.repository.implementacaoLancamento;

import com.exemplo.algamoney.api.dto.LancamentoEstatisticaCategoria;
import com.exemplo.algamoney.api.model.Lancamento;
import com.exemplo.algamoney.api.repository.filter.LancamentoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface LancamentoRepositoryQuery {

    public List<LancamentoEstatisticaCategoria> porCategoria(LocalDate mesReferencia);

    public Page<Lancamento> filtrate(LancamentoFilter lancamentoFilter, Pageable pageable);

}
