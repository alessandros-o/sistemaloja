package com.alessandro.sistemaloja.resource;

import com.alessandro.sistemaloja.domain.Pedido;
import com.alessandro.sistemaloja.dto.CategoriaResponse;
import com.alessandro.sistemaloja.service.PedidoService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

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

    @PostMapping
    public ResponseEntity<Void> inserir(@Valid @RequestBody Pedido obj) {
        obj = service.inserir(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<Page<Pedido>> listarTodasPaginado(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "qtdPorPagina", defaultValue = "24") Integer qtdPorPagina,
            @RequestParam(value = "orderBy", defaultValue = "instante") String orderBy,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction) {
        Page<Pedido> list = service.buscarPaginado(page, qtdPorPagina, orderBy, direction);
        return ResponseEntity.ok().body(list);
    }
}
