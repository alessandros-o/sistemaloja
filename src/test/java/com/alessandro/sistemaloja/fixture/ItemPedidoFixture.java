package com.alessandro.sistemaloja.fixture;

import com.alessandro.sistemaloja.domain.ItemPedido;
import com.alessandro.sistemaloja.domain.ItemPedidoPK;
import lombok.experimental.UtilityClass;

import java.text.ParseException;

@UtilityClass
public class ItemPedidoFixture {

    public static ItemPedido itemPedidoValido() throws ParseException {

        return new ItemPedido(
                PedidoFixture.pedidoWithoutItens(),
                ProdutoFixture.produtoWithoutItens(),
                0.00,
                2,
                2000.00
                );
    }

    public static ItemPedido itemPedidoWithouPedidoAndProduto() {
        return ItemPedido.builder()
                .id(new ItemPedidoPK())
                .preco(2000.00)
                .quantidade(2)
                .desconto(0.00)
                .build();
    }
}
