package com.alessandro.sistemaloja.service;

import com.alessandro.sistemaloja.domain.Cliente;
import com.alessandro.sistemaloja.domain.Cliente;
import com.alessandro.sistemaloja.dto.ClienteResponse;
import com.alessandro.sistemaloja.repository.ClienteRepository;
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
public class ClienteService {

    private final ClienteRepository repo;

    public ClienteService(ClienteRepository repo) {
        this.repo = repo;
    }

    public Cliente buscarPorId(Integer id) {

        Optional<Cliente> clienteOptional = repo.findById(id);
        return clienteOptional.orElseThrow(() -> new ObjectNotFoundException(Cliente.class, "Cliente com id: " + id +  " não encontrado!"));
    }

    public Cliente alterar(Cliente obj) {
        var newObj = buscarPorId(obj.getId());
        updateData(newObj, obj);
        return repo.save(newObj);
    }

    public void deletar(Integer id) {
        buscarPorId(id);
        try {
            repo.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir um cliente que possui entidades relacionadas");
        }

    }

    public List<ClienteResponse> listarTodas() {
        var list = repo.findAll();
        List<ClienteResponse> responseList = list.stream().map(x -> new ClienteResponse(x)).collect(Collectors.toList());
        return  responseList;
    }

    public Page<ClienteResponse> buscarPaginado(Integer page, Integer qtdPorPagina, String orderBy, String direction) {
        PageRequest pageRequest = PageRequest.of(page, qtdPorPagina, Sort.Direction.valueOf(direction), orderBy);
        var objetoPaginado = repo.findAll(pageRequest);
        Page<ClienteResponse> responsePage = objetoPaginado.map(x -> new ClienteResponse(x));
        return responsePage;
    }

    public Cliente fromDTO(ClienteResponse response) {

        return new Cliente(response.getId(), response.getNome(), response.getEmail(), null, null);
    }

    private void updateData(Cliente newObj, Cliente obj) {
        newObj.setNome(obj.getNome());
        newObj.setEmail(obj.getEmail());
    }
}
