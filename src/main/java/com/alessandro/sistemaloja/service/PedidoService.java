package com.alessandro.sistemaloja.service;

import com.alessandro.sistemaloja.domain.Pedido;
import com.alessandro.sistemaloja.repository.PedidoRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public Pedido buscarPorId(Integer id) {
        Optional<Pedido> obj = pedidoRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(Pedido.class, "Pedido de id " + id + " não encontrado"));
    }
}
