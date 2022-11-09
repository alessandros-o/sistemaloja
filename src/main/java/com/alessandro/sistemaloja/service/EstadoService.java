package com.alessandro.sistemaloja.service;

import com.alessandro.sistemaloja.domain.Estado;
import com.alessandro.sistemaloja.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstadoService {

    @Autowired
    private EstadoRepository repo;

    public List<Estado> listarTodos() {
        return repo.findAllByOrderByNome();
    }
}
