package com.alessandro.sistemaloja.fixture;

import com.alessandro.sistemaloja.domain.Categoria;
import lombok.experimental.UtilityClass;

import java.util.Arrays;

@UtilityClass
public class CategoriaFixture {

    public static Categoria categoriaValida() {
        return Categoria.builder()
                .id(1)
                .nome("Informática")
                .produtos(null)
                .build();
    }
}
