package com.alessandro.sistemaloja.service;

import com.alessandro.sistemaloja.domain.ItemPedido;
import com.alessandro.sistemaloja.domain.PagamentoComBoleto;
import com.alessandro.sistemaloja.domain.Pedido;
import com.alessandro.sistemaloja.domain.enums.EstadoPagamento;
import com.alessandro.sistemaloja.repository.ItemPedidoRepository;
import com.alessandro.sistemaloja.repository.PagamentoRepository;
import com.alessandro.sistemaloja.repository.PedidoRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository repo;
    private final PagamentoRepository pagamentoRepository;
    private final ProdutoService produtoService;
    private final ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private BoletoService boletoService;

    public PedidoService(PedidoRepository pedidoRepository, PagamentoRepository pagamentoRepository, ProdutoService produtoService, ItemPedidoRepository itemPedidoRepository) {
        this.repo = pedidoRepository;
        this.pagamentoRepository = pagamentoRepository;
        this.produtoService = produtoService;
        this.itemPedidoRepository = itemPedidoRepository;
    }

    public Pedido buscarPorId(Integer id) {
        Optional<Pedido> obj = repo.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(Pedido.class, "Pedido de id " + id + " não encontrado"));
    }

    public Pedido inserir(Pedido obj) {
        obj.setId(null);
        obj.setInstante(new Date());
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
            ip.setPreco(produtoService.buscarPorId(ip.getProduto().getId()).getPreco());
            ip.setPedido(obj);
        }
        itemPedidoRepository.saveAll(obj.getItens());
        return obj;
    }
}
