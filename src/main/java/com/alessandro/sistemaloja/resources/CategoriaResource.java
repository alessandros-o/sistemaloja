package com.alessandro.sistemaloja.resources;

import com.alessandro.sistemaloja.domain.Categoria;
import com.alessandro.sistemaloja.service.CategoriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {

    private final CategoriaService service;

    public CategoriaResource(CategoriaService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> buscarPorId(@PathVariable Integer id) {

        Categoria obj = service.buscarPorId(id);
        return ResponseEntity.ok().body(obj);
    }
}
