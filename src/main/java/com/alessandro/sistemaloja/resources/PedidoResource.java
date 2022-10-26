package com.alessandro.sistemaloja.resources;

import com.alessandro.sistemaloja.domain.Categoria;
import com.alessandro.sistemaloja.domain.Pedido;
import com.alessandro.sistemaloja.service.CategoriaService;
import com.alessandro.sistemaloja.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoResource {

    private final PedidoService service;

    public PedidoResource(PedidoService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Integer id) {

        Pedido obj = service.buscarPorId(id);
        return ResponseEntity.ok().body(obj);
    }
}
