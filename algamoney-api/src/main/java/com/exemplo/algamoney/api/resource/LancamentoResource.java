package com.exemplo.algamoney.api.resource;


import com.exemplo.algamoney.api.exceptionhandler.AlgamoneyExceptionHandler;
import com.exemplo.algamoney.api.model.Lancamento;
import com.exemplo.algamoney.api.repository.LancamentoRepository;
import com.exemplo.algamoney.api.repository.filter.LancamentoFilter;
import com.exemplo.algamoney.api.service.LancamentoService;
import com.exemplo.algamoney.api.service.exception.PessoaInexistenteOuInativaException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/lancamento")
public class LancamentoResource {

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private LancamentoService lancamentoService;

    @Autowired
    private MessageSource messageSource;

//    @GetMapping
//    public List<Lancamento> buscarTodos(){
//        return lancamentoRepository.findAll();
//    }

    @GetMapping
    public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable){
        return lancamentoRepository.filtrate(lancamentoFilter, pageable);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Lancamento> buscarPorId(@PathVariable Long codigo){

        return lancamentoRepository.findById(codigo)
                .map(pessoa -> ResponseEntity.ok(pessoa))
                .orElse(ResponseEntity.notFound().build());
    }



    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarLancamento(@PathVariable Long codigo){
        lancamentoRepository.deleteById(codigo);
    }



    @PostMapping
    public ResponseEntity<Lancamento> cadastrar(@Valid @RequestBody Lancamento lancamento){

        Lancamento lancamentoSalvo = lancamentoService.save(lancamento);

        //esse Uri é o responsável por criar o cabeçalho
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
                .buildAndExpand(lancamentoSalvo.getCodigo()).toUri();
        return ResponseEntity.created(uri).body(lancamentoSalvo);
    }


    //Exceção Unica que foi criada para Não deixar salvar com uma Pessoa inativa.
    @ExceptionHandler({PessoaInexistenteOuInativaException.class})
    public ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex){
        String mensagemUsuario = messageSource.getMessage("pessoa-inexistente-ou-inativa",null, LocaleContextHolder.getLocale());
        String mensagemDesenvolvedor = ex.toString();
        List<AlgamoneyExceptionHandler.Erro> erros = Arrays.asList(new AlgamoneyExceptionHandler.Erro(mensagemUsuario, mensagemDesenvolvedor));

        return ResponseEntity.badRequest().body(erros);
    }



}
