package com.alessandro.sistemaloja.fixture;

import com.alessandro.sistemaloja.domain.Endereco;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EnderecoFixture {

    public static Endereco enderecoValido() {
        return Endereco.builder()
                .id(null)
                .logradouro("Rua do nunca")
                .numero("159")
                .complemento("não")
                .cep("12305809")
                .bairro("Qualquer")
                .cidade(CidadeFixture.cidadeValida())
                .cliente(null)
                .build();
    }
}
