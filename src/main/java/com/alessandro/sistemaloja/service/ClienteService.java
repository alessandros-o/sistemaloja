package com.alessandro.sistemaloja.service;

import com.alessandro.sistemaloja.domain.Cliente;
import com.alessandro.sistemaloja.repository.ClienteRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente buscarPorId(Integer id) {

        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        return clienteOptional.orElseThrow(() -> new ObjectNotFoundException(Cliente.class, "Cliente com id: " + id +  " não encontrado!"));
    }
}
