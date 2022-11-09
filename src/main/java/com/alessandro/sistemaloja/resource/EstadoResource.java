package com.alessandro.sistemaloja.resource;

import com.alessandro.sistemaloja.dto.CidadeDTO;
import com.alessandro.sistemaloja.dto.EstadoDTO;
import com.alessandro.sistemaloja.service.CidadeService;
import com.alessandro.sistemaloja.service.EstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/estados")
public class EstadoResource {

    @Autowired
    private EstadoService service;

    @Autowired
    private CidadeService cidadeService;

    @GetMapping
    public ResponseEntity<List<EstadoDTO>> listarTodos() {
        var list = service.listarTodos();
        List<EstadoDTO> listaDto = list.stream().map(x -> new EstadoDTO(x)).collect(Collectors.toList());
        return ResponseEntity.ok().body(listaDto);
    }

    @GetMapping(value = "/{estadoId}/cidades")
    public ResponseEntity<List<CidadeDTO>> buscarCidadePorEstado(@PathVariable Integer estadoId) {
        var list = cidadeService.buscarCidadePorEstado(estadoId);
        List<CidadeDTO> listDto = list.stream().map(x -> new CidadeDTO(x)).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
