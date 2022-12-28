package com.alessandro.sistemaloja.fixture;

import com.alessandro.sistemaloja.domain.ItemPedido;
import com.alessandro.sistemaloja.domain.Pagamento;
import com.alessandro.sistemaloja.domain.PagamentoComCartao;
import com.alessandro.sistemaloja.domain.Pedido;
import com.alessandro.sistemaloja.domain.enums.EstadoPagamento;
import lombok.experimental.UtilityClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@UtilityClass
public class PedidoFixture {

    public static Pedido pedidoValido() throws ParseException {
        return Pedido.builder()
                .id(1)
                .cliente(ClienteFixture.clienteValido())
                .enderecoDeEntrega(EnderecoFixture.enderecoValido())
                .instante(Date.from(Instant.now()))
                .pagamento(pagamentoCartao())
                .itens(Collections.singleton(ItemPedidoFixture.itemPedidoValido()))
                .build();
    }

    public static Pedido pedidoWithoutItens() throws ParseException {
        return Pedido.builder()
                .id(1)
                .cliente(ClienteFixture.clienteValido())
                .enderecoDeEntrega(EnderecoFixture.enderecoValido())
                .instante(Date.from(Instant.now()))
                .pagamento(pagamentoCartao())
                .itens(null)
                .build();
    }

    public Pagamento pagamentoCartao() throws ParseException {
        return new PagamentoComCartao(1, EstadoPagamento.QUITADO, null, 6);
    }
}
