package com.alessandro.sistemaloja.service;

import com.alessandro.sistemaloja.domain.Cidade;
import com.alessandro.sistemaloja.repository.CidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CidadeService {

    @Autowired
    private CidadeRepository repo;

    public List<Cidade> buscarCidadePorEstado(Integer estadoId) {
        return repo.findCidades(estadoId);
    }
}
