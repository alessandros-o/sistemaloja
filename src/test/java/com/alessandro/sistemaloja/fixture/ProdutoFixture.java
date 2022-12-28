package com.alessandro.sistemaloja.fixture;

import com.alessandro.sistemaloja.domain.ItemPedido;
import com.alessandro.sistemaloja.domain.Produto;
import lombok.experimental.UtilityClass;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

@UtilityClass
public class ProdutoFixture {

    public static Produto produtoValido() throws ParseException {
        return Produto.builder()
                .id(1)
                .nome("Notebook")
                .preco(3000.00)
                .categorias(Arrays.asList(CategoriaFixture.categoriaValida()))
                .itens(Collections.singleton(ItemPedidoFixture.itemPedidoWithouPedidoAndProduto()))
                .build();
    }

    public static Produto produtoWithoutItens() {
        return Produto.builder()
                .id(1)
                .nome("Notebook")
                .preco(3000.00)
                .categorias(Arrays.asList(CategoriaFixture.categoriaValida()))
                .itens(null)
                .build();
    }
}
