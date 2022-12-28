package com.alessandro.sistemaloja.service;

import com.alessandro.sistemaloja.domain.Cliente;
import com.alessandro.sistemaloja.domain.ItemPedido;
import com.alessandro.sistemaloja.domain.PagamentoComBoleto;
import com.alessandro.sistemaloja.domain.Pedido;
import com.alessandro.sistemaloja.domain.enums.EstadoPagamento;
import com.alessandro.sistemaloja.dto.AuthenticatedUserDetails;
import com.alessandro.sistemaloja.repository.ItemPedidoRepository;
import com.alessandro.sistemaloja.repository.PagamentoRepository;
import com.alessandro.sistemaloja.repository.PedidoRepository;
import com.alessandro.sistemaloja.service.exception.AuthorizationException;
import org.hibernate.ObjectNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository repo;
    private final PagamentoRepository pagamentoRepository;
    private final ProdutoService produtoService;
    private final ItemPedidoRepository itemPedidoRepository;

    private BoletoService boletoService;

    private ClienteService clienteService;

    private EmailService emailService;

    public PedidoService(PedidoRepository pedidoRepository, PagamentoRepository pagamentoRepository,
                         ProdutoService produtoService, ItemPedidoRepository itemPedidoRepository,
                         ClienteService clienteService, BoletoService boletoService, EmailService emailService) {
        this.repo = pedidoRepository;
        this.pagamentoRepository = pagamentoRepository;
        this.produtoService = produtoService;
        this.itemPedidoRepository = itemPedidoRepository;
        this.clienteService = clienteService;
        this.boletoService = boletoService;
        this.emailService = emailService;
    }

    public Pedido buscarPorId(Integer id) {
        Optional<Pedido> obj = repo.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(Pedido.class, "Pedido de id " + id + " não encontrado"));
    }

    @Transactional
    public Pedido inserir(Pedido obj) {
        obj.setId(null);
        obj.setInstante(new Date());
        obj.setCliente(clienteService.buscarPorId(obj.getCliente().getId()));
        obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
        obj.getPagamento().setPedido(obj);
        if (obj.getPagamento() instanceof PagamentoComBoleto) {
            PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
            boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
        }
        obj = repo.save(obj);
        pagamentoRepository.save(obj.getPagamento());
        for (ItemPedido ip : obj.getItens()) {
            ip.setDesconto(0.0);
            ip.setProduto(produtoService.buscarPorId(ip.getProduto().getId()));
            ip.setPreco(ip.getProduto().getPreco());
            ip.setPedido(obj);
        }
        itemPedidoRepository.saveAll(obj.getItens());
        emailService.sendOrderConfirmationEmail(obj);
        return obj;
    }

    public Page<Pedido> buscarPaginado(Integer page, Integer qtdPorPagina, String orderBy, String direction) {

        AuthenticatedUserDetails user = UserDetailsService.authenticated();
        if (user == null) {
            throw new AuthorizationException("Acesso negado!");
        }
        PageRequest pageRequest = PageRequest.of(page, qtdPorPagina, Sort.Direction.valueOf(direction), orderBy);
        Cliente cliente = clienteService.buscarPorId(user.id());
        return repo.findByCliente(cliente, pageRequest);
    }
}
