package com.alessandro.sistemaloja.resource;

import com.alessandro.sistemaloja.domain.Produto;
import com.alessandro.sistemaloja.dto.ProdutoResponse;
import com.alessandro.sistemaloja.resource.utils.URL;
import com.alessandro.sistemaloja.service.ProdutoService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/produtos")
public class ProdutoResource {

    private final ProdutoService service;

    public ProdutoResource(ProdutoService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Integer id) {

        Produto obj = service.buscarPorId(id);
        return ResponseEntity.ok().body(obj);
    }

    @GetMapping
    public ResponseEntity<Page<ProdutoResponse>> listarTodasPaginado(
            @RequestParam(value = "nome", defaultValue = "") String nome,
            @RequestParam(value = "categorias", defaultValue = "") String categorias,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "qtdPorPagina", defaultValue = "24") Integer qtdPorPagina,
            @RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        String nomeDecoded = URL.decodeParam(nome);
        List<Integer> ids = URL.decodeIntList(categorias);
        Page<Produto> pageProdutos = service.buscarPaginado(nomeDecoded, ids, page, qtdPorPagina, orderBy, direction);
        Page<ProdutoResponse> listResponse = pageProdutos.map(x -> new ProdutoResponse(x));
        return ResponseEntity.ok().body(listResponse);
    }

}
