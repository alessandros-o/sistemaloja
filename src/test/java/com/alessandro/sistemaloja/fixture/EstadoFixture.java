package com.alessandro.sistemaloja.fixture;

import com.alessandro.sistemaloja.domain.Estado;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Collections;

@UtilityClass
public class EstadoFixture {

    public static Estado estadoValido() {
        return new Estado(null, "Paulo");
    }

}
