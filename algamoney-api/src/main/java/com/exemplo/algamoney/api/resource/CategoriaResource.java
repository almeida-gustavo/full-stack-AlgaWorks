package com.exemplo.algamoney.api.resource;

import com.exemplo.algamoney.api.event.RecursoCriadoEvent;
import com.exemplo.algamoney.api.model.Categoria;
import com.exemplo.algamoney.api.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categoria")
public class CategoriaResource {

    @Autowired
    private CategoriaRepository categoriaRepository;

//  Isso é para poder chamar o eveneto ao invés de ficar usando o Uri uri = ....
    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping
    public List<Categoria> listar(){
        return categoriaRepository.findAll();
    }
//    Essa forma é a mesma que de cima, só que vc está retornando alguns tipos de mensagens para validação
//    @GetMapping
//    public ResponseEntity<?> listar(){
//        List<Categoria> categorias = categoriaRepository.findAll();
//        return categorias.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(categorias);
//    }

    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED) Você pode passar aqui se não for usar o ResponseEntity como abaixo
    public ResponseEntity<Categoria> criarCategoria(@Valid @RequestBody Categoria categoria, HttpServletResponse response){
      Categoria categoriaSalva =  categoriaRepository.save(categoria);

      //Essa parte abaixo é para que você consiga construir uma resposta de cabeçalho quando salvar uma catégoria
      URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
                .buildAndExpand(categoriaSalva.getCodigo()).toUri();
//    response.setHeader("Location", uri.toASCIIString()); vc pode usar esse se não utilizar o ResponseEntity abaixo
      return ResponseEntity.created(uri).body(categoriaSalva);

//        Essa forma abaixo não está mais funcionando o envio do header.
//        publisher.publishEvent(new RecursoCriadoEvent(this, response, categoriaSalva.getCodigo()));
//        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity buscarPeloCodigo(@PathVariable Long codigo){

//        Tanto esse quanto o de baixo faz a mesma coisa, são apenas dois métodos diferentes.
//        Optional categoria = this.categoriaRepository.findById(codigo);
//        return categoria.isPresent() ? ResponseEntity.ok(categoria.get()) : ResponseEntity.notFound().build();

        return this.categoriaRepository.findById(codigo)
                .map(categoria -> ResponseEntity.ok(categoria))
                .orElse(ResponseEntity.notFound().build());
    }

}
