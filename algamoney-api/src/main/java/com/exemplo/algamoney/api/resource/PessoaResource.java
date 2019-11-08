package com.exemplo.algamoney.api.resource;

import com.exemplo.algamoney.api.event.RecursoCriadoEvent;
import com.exemplo.algamoney.api.model.Pessoa;
import com.exemplo.algamoney.api.repository.PessoaRepository;
import com.exemplo.algamoney.api.service.PessoaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pessoa")
public class PessoaResource {

    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping
    public List<Pessoa> searchAll(){
        return pessoaRepository.findAll();
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Pessoa> searchOnePessoa(@PathVariable Long codigo){

        return pessoaRepository.findById(codigo).map(pessoa -> ResponseEntity.ok(pessoa))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pessoa> addPessoa(@Valid @RequestBody Pessoa pessoa, HttpServletResponse response){
        Pessoa pessoaSalva = pessoaRepository.save(pessoa);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
                .buildAndExpand(pessoaSalva.getCodigo()).toUri();
        return ResponseEntity.created(uri).body(pessoaSalva);

//        Essa forma abaixo não está mais funcionando o envio do header.
//        publisher.publishEvent(new RecursoCriadoEvent(this, response, pessoaSalva.getCodigo()));
//        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
    }

    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePessoa(@PathVariable Long codigo){
        pessoaRepository.deleteById(codigo);
//  mais código de tratamento de erro lá no AlgamoneyExceptionHandler -> handleEmptyResultDataAccessException
    }


    @PutMapping("/{codigo}")
    public ResponseEntity<Pessoa> updatePessoa(@PathVariable Long codigo, @Valid @RequestBody Pessoa pessoa){
        Pessoa pessoaSalva = pessoaService.atualizar(codigo, pessoa);

        return ResponseEntity.ok(pessoaSalva);
    }

//  Atualizando apenas 1 propriedade sem ter que enviar todas as outras que são obrigatórias
    @PutMapping("/{codigo}/ativo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePropertyAtivo(@PathVariable Long codigo, @RequestBody Boolean ativo){
        pessoaService.atualizarPropriedadeAtivo(codigo, ativo);

    }



}
