package com.exemplo.algamoney.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

@Embeddable
@Getter @Setter
public class Endereco {

    @Size(max = 50)
    private String logradouro;

    @Size(max = 10)
    private String numero;

    @Size(max = 50)
    private String complemento;

    @Size(max = 50)
    private String bairro;

    @Size(max = 12)
    private String cep;

    @Size(max = 50)
    private String cidade;

    @Size(max = 15)
    private String estado;


}

