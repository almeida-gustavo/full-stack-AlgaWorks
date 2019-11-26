package com.exemplo.algamoney.api.service;

import com.exemplo.algamoney.api.model.Pessoa;
import com.exemplo.algamoney.api.repository.PessoaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;


    public Pessoa atualizar(Long codigo, Pessoa pessoa){
        Pessoa pessoaSalva = buscarPessoaPeloCodigo(codigo);

//Explicação dos campos: 1: dados que vc quer passar, 2: para onde vc quer passar os dados, 3: ignorando quais propriedades. como vc não passa o id vc vai ignorar ele
        BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo");
        return this.pessoaRepository.save(pessoaSalva);
    }


    public void atualizarPropriedadeAtivo(Long codigo, Boolean ativo){
        Pessoa pessoaSalva = buscarPessoaPeloCodigo(codigo);

        pessoaSalva.setAtivo(ativo);
        pessoaRepository.save(pessoaSalva);

    }

//   Neste caso, utilizamos o método orElseThrow(...) de Optional, o que significa que caso o Optional obtido pela consulta esteja sem conteúdo, iremos lançar uma exceção.
    public Pessoa buscarPessoaPeloCodigo(Long codigo) {
        return this.pessoaRepository.findById(codigo)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

}
