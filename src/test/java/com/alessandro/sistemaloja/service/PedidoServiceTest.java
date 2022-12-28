package com.alessandro.sistemaloja.service;

import com.alessandro.sistemaloja.domain.Pagamento;
import com.alessandro.sistemaloja.domain.Pedido;
import com.alessandro.sistemaloja.fixture.ClienteFixture;
import com.alessandro.sistemaloja.fixture.ItemPedidoFixture;
import com.alessandro.sistemaloja.fixture.PedidoFixture;
import com.alessandro.sistemaloja.fixture.ProdutoFixture;
import com.alessandro.sistemaloja.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private PagamentoRepository pagamentoRepository;

    @Mock
    private ItemPedidoRepository itemPedidoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private PedidoService pedidoService;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private BoletoService boletoService;

    @Mock
    private ProdutoService produtoService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private MockEmailService mockEmailService;

    @Test
    void should_buscarPorId_ThenReturn_Valid() throws ParseException {
        Integer id = 1;
        Optional<Pedido> optionalPedido =Optional.of(PedidoFixture.pedidoValido());

        when(pedidoRepository.findById(any())).thenReturn(optionalPedido);

        var pedido =pedidoService.buscarPorId(id);

        verify(pedidoRepository).findById(id);
        assertEquals("Alessandro", pedido.getCliente().getNome());
    }

    @Test
    void should_inserir_ThenReturn_PedidoCriado() throws ParseException {
        final var expectedPedido = PedidoFixture.pedidoValido();
        final var validCliente = ClienteFixture.clienteValido();

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(expectedPedido);
        when(clienteService.buscarPorId(validCliente.getId())).thenReturn(validCliente);
        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(PedidoFixture.pagamentoCartao());
        when(produtoService.buscarPorId(any(Integer.class))).thenReturn(ProdutoFixture.produtoValido());
        when(itemPedidoRepository.saveAll(any())).thenReturn(Arrays.asList(ItemPedidoFixture.itemPedidoValido()));
        doNothing().when(emailService).sendOrderConfirmationEmail(expectedPedido);

        var actualResponse = pedidoService.inserir(PedidoFixture.pedidoValido());

        verify(pedidoRepository, times(1)).save(any(Pedido.class));
        assertNotNull(actualResponse.getId());
        assertEquals(expectedPedido.getCliente(), actualResponse.getCliente());
        assertEquals(expectedPedido.getPagamento(), actualResponse.getPagamento());
    }

    @Test
    void buscarPaginado() {
    }
}