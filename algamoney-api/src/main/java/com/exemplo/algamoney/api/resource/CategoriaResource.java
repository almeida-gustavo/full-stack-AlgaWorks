package com.exemplo.algamoney.api.resource;

import com.exemplo.algamoney.api.event.RecursoCriadoEvent;
import com.exemplo.algamoney.api.model.Categoria;
import com.exemplo.algamoney.api.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

//  Isso é para poder chamar o evento ao invés de ficar usando o Uri uri = ....
    @Autowired
    private ApplicationEventPublisher publisher;

    //VOCÊ PODE CRIAR O PAGE DIRETO AQUI AO INVÉS DA LISTA SEM TER QUE IMPLEMENTAR O FILTER E AS QUERYS
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA') and #oauth2.hasScope('read')")
    public Page<Categoria> listar(Pageable pageable){
        return categoriaRepository.findAll(pageable);
    }
//    Essa forma é a mesma que de cima, só que vc está retornando alguns tipos de mensagens para validação
//    @GetMapping
//    public ResponseEntity<?> listar(){
//        List<Categoria> categorias = categoriaRepository.findAll();
//        return categorias.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(categorias);
//    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA') and #oauth2.hasScope('write')")
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
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA') and #oauth2.hasScope('read')")
    public ResponseEntity buscarPeloCodigo(@PathVariable Long codigo){

//        Tanto esse quanto o de baixo faz a mesma coisa, são apenas dois métodos diferentes.
//        Optional categoria = this.categoriaRepository.findById(codigo);
//        return categoria.isPresent() ? ResponseEntity.ok(categoria.get()) : ResponseEntity.notFound().build();

        return this.categoriaRepository.findById(codigo)
                .map(categoria -> ResponseEntity.ok(categoria))
                .orElse(ResponseEntity.notFound().build());

//        Neste exemplo, fizemos a chamada ao método findById que nos retornou um Optional do tipo Categoria.
//        Usamos o método map, para transformar o objeto que foi retornado como Optional (isso é feito caso o mesmo não seja null), depois retorna o objeto transformado em Optional novamente.
//        Como o retorno do próprio map também é um Optional, podemos utilizar o método orElse, para retornarmos notFound, como mostrado na imagem acima.
    }



}
