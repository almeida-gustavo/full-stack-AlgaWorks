package com.exemplo.algamoney.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "pessoa")
@Getter @Setter
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @NotNull
    @Size(max = 50)
    private String nome;

    @NotNull
    private Boolean ativo;

    @Embedded
    private Endereco endereco;

    @JsonIgnore
    @Transient
    public boolean isInativo(){
        return !this.ativo;
    }



}
