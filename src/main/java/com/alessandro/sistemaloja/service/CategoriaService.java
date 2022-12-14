package com.alessandro.sistemaloja.service;

import com.alessandro.sistemaloja.domain.Categoria;
import com.alessandro.sistemaloja.domain.Cliente;
import com.alessandro.sistemaloja.dto.CategoriaResponse;
import com.alessandro.sistemaloja.repository.CategoriaRepository;
import com.alessandro.sistemaloja.service.exception.DataIntegrityException;
import org.hibernate.ObjectNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        var newObj = buscarPorId(obj.getId());
        updateData(newObj, obj);
        return repo.save(newObj);
    }

    public void deletar(Integer id) {
        buscarPorId(id);
        try {
            repo.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir uma categoria que possui produtos");
        }

    }

    public List<CategoriaResponse> listarTodas() {
        var list = repo.findAll();
        List<CategoriaResponse> responseList = list.stream().map(x -> new CategoriaResponse(x)).collect(Collectors.toList());
        return  responseList;
    }

    public Page<CategoriaResponse> buscarPaginado(Integer page, Integer qtdPorPagina, String orderBy, String direction) {
        PageRequest pageRequest = PageRequest.of(page, qtdPorPagina, Sort.Direction.valueOf(direction), orderBy);
        var objetoPaginado = repo.findAll(pageRequest);
        Page<CategoriaResponse> responsePage = objetoPaginado.map(x -> new CategoriaResponse(x));
        return responsePage;
    }

    public Categoria fromDTO(CategoriaResponse response) {
        return new Categoria(response.getId(), response.getNome());
    }

    private void updateData(Categoria newObj, Categoria obj) {
        newObj.setNome(obj.getNome());
    }
}
