package com.exemplo.algamoney.api.repository;

import com.exemplo.algamoney.api.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//Long é o tipo de dado da chave primaria do Model Categoria
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {


}
