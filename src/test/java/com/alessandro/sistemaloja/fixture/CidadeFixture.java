package com.alessandro.sistemaloja.fixture;

import com.alessandro.sistemaloja.domain.Cidade;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CidadeFixture {

    public static Cidade cidadeValida() {
        return Cidade.builder()
                .id(1)
                .nome("Jacareí")
                .estado(EstadoFixture.estadoValido())
                .build();
    }
}
