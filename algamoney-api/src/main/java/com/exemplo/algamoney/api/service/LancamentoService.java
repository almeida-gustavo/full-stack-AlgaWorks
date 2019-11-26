package com.exemplo.algamoney.api.service;

import com.exemplo.algamoney.api.model.Lancamento;
import com.exemplo.algamoney.api.model.Pessoa;
import com.exemplo.algamoney.api.repository.LancamentoRepository;
import com.exemplo.algamoney.api.repository.PessoaRepository;
import com.exemplo.algamoney.api.service.exception.PessoaInexistenteOuInativaException;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class LancamentoService {

    @Autowired
    private LancamentoRepository lancamentoRepository;
    @Autowired
    private PessoaRepository pessoaRepository;

    public Lancamento save(Lancamento lancamento) {
        Pessoa pessoa = pessoaRepository.findById(lancamento.getPessoa().getCodigo())
                .orElseThrow(() -> new EmptyResultDataAccessException(1));

        if(pessoa == null || !pessoa.getAtivo()){
            //Exceção criada para essa situação Especifica.
            throw new PessoaInexistenteOuInativaException();
        }
        return lancamentoRepository.save(lancamento);
    }
}
