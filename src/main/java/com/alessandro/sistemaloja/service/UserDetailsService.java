package com.alessandro.sistemaloja.service;

import com.alessandro.sistemaloja.domain.Cliente;
import com.alessandro.sistemaloja.dto.AuthenticatedUserDetails;
import com.alessandro.sistemaloja.repository.ClienteRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService {

    private final ClienteRepository repo;

    public UserDetailsService(ClienteRepository repo) {
        this.repo = repo;
    }

    public Cliente buscarPorEmail(String email) {
       return repo.findByEmail(email);
    }

    public static AuthenticatedUserDetails authenticated() {
        try {
            return (AuthenticatedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        catch (Exception e) {
            return null;
        }

    }
}
