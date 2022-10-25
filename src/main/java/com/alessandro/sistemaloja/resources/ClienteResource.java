package com.alessandro.sistemaloja.resources;

import com.alessandro.sistemaloja.domain.Cliente;
import com.alessandro.sistemaloja.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {

    private final ClienteService clienteService;

    public ClienteResource(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Integer id) {
        Cliente obj = clienteService.buscarPorId(id);
        return ResponseEntity.ok().body(obj);
    }
}
