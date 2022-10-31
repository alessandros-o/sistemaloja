package com.alessandro.sistemaloja.service;

import com.alessandro.sistemaloja.domain.Categoria;
import com.alessandro.sistemaloja.domain.Pedido;
import com.alessandro.sistemaloja.domain.Produto;
import com.alessandro.sistemaloja.repository.CategoriaRepository;
import com.alessandro.sistemaloja.repository.PedidoRepository;
import com.alessandro.sistemaloja.repository.ProdutoRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository repo;
    private final CategoriaRepository categoriaRepository;

    public ProdutoService(ProdutoRepository repo, CategoriaRepository categoriaRepository) {
        this.repo = repo;
        this.categoriaRepository = categoriaRepository;
    }

    public Produto buscarPorId(Integer id) {
        Optional<Produto> obj = repo.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(Produto.class, "Produto de id " + id + " não encontrado"));
    }

    public Page<Produto> buscarPaginado(String nome, List<Integer> ids, Integer page, Integer qtdPorPagina, String orderBy, String direction) {
        PageRequest pageRequest = PageRequest.of(page, qtdPorPagina, Sort.Direction.valueOf(direction), orderBy);
        List<Categoria> categorias = categoriaRepository.findAllById(ids);
        return repo.search(nome, categorias, pageRequest);
    }
}
