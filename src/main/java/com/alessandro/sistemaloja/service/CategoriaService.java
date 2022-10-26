package com.alessandro.sistemaloja.service;

import com.alessandro.sistemaloja.domain.Categoria;
import com.alessandro.sistemaloja.repository.CategoriaRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoriaService {

    private final CategoriaRepository repo;

    public CategoriaService(CategoriaRepository repo) {
        this.repo = repo;
    }

    public Categoria buscarPorId(Integer id) {
        Optional<Categoria> obj = repo.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(Categoria.class,
                "Objeto não encontrado! Id: " + id + " Tipo: " + Categoria.class.getName()));
    }

    public Categoria inserir(Categoria obj) {
        return repo.save(obj);
    }

    public Categoria alterar(Categoria obj) {

        buscarPorId(obj.getId());
        return repo.save(obj);
    }
}
