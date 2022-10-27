package com.alessandro.sistemaloja.dto;

import com.alessandro.sistemaloja.domain.Categoria;

import java.io.Serial;
import java.io.Serializable;

public class CategoriaResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String nome;

    public CategoriaResponse() {
    }

    public CategoriaResponse(Categoria categoria) {
        this.id = categoria.getId();
        this.nome = categoria.getNome();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
