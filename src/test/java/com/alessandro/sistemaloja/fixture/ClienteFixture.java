package com.alessandro.sistemaloja.fixture;

import com.alessandro.sistemaloja.domain.Cliente;
import com.alessandro.sistemaloja.domain.enums.Perfil;
import com.alessandro.sistemaloja.domain.enums.TipoCliente;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ClienteFixture {

    public static Cliente clienteValido(){

        Cliente cliente = new Cliente(
                1,
                "Alessandro",
                "ale@gmail.com",
                "75128807070",
                TipoCliente.PESSOAFISICA,
                "password"
        );
        return cliente;
    }
}
