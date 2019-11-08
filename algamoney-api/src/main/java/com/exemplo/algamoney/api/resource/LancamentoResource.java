package com.exemplo.algamoney.api.resource;


import com.exemplo.algamoney.api.model.Lancamento;
import com.exemplo.algamoney.api.repository.LancamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/lancamento")
public class LancamentoResource {

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @GetMapping
    public List<Lancamento> buscarTodos(){
        return lancamentoRepository.findAll();
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Lancamento> buscarPorId(@PathVariable Long codigo){

        return lancamentoRepository.findById(codigo)
                .map(pessoa -> ResponseEntity.ok(pessoa))
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<Lancamento> cadastrar(@Valid @RequestBody Lancamento lancamento){

        Lancamento lancamentoSalvo = lancamentoRepository.save(lancamento);

        //esse Uri é o responsável por criar o cabeçalho
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
                .buildAndExpand(lancamentoSalvo.getCodigo()).toUri();
        return ResponseEntity.created(uri).body(lancamentoSalvo);
    }


}
