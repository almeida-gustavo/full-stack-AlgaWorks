package com.exemplo.algamoney.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "usuario")
@Getter @Setter
public class Usuario {

    @Id //o banco de dados foi criado informando o ID então ele não deve ser incrementado automatico
    private Long codigo;

    private String nome;
    private String email;
    private String senha;

    @ManyToMany(fetch = FetchType.EAGER) //toda vez que buscar o usuário já traz as permissões dele
    @JoinTable(name = "usuario_permissao", joinColumns = @JoinColumn(name = "codigo_usuario"),
        inverseJoinColumns = @JoinColumn(name = "codigo_permissao")) //tabela de relacionamento
    //vc ta colocando la na tabela usuario_permissao o codigo do usuario, e o inverse é para falar qual coluna que faz o trabalho contrario
    private List<Permissao> permissoes;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(getCodigo(), usuario.getCodigo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCodigo());
    }
}
