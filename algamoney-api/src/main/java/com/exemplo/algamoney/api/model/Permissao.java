package com.exemplo.algamoney.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "permissao")
@Getter @Setter
public class Permissao {

    @Id //esse só tem o ID porque ele não colocou para ser como auto incremento no BD
    private Long codigo;

    private String descricao;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Permissao)) return false;
        Permissao permissao = (Permissao) o;
        return Objects.equals(getCodigo(), permissao.getCodigo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCodigo());
    }
}
