package com.alessandro.sistemaloja.service;

import com.alessandro.sistemaloja.domain.Cliente;
import com.alessandro.sistemaloja.dto.LoginRequest;
import com.alessandro.sistemaloja.security.JWTUtil;
import org.hibernate.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
public class LoginService {

    private final JWTUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public LoginService(JWTUtil jwtUtil, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(LoginRequest loginRequest) {
        Cliente cliente = userDetailsService.buscarPorEmail(loginRequest.email());

        if (Objects.isNull(cliente)) {
            throw new ObjectNotFoundException(HttpStatus.NOT_FOUND, "Cliente não encontrado");
        }

        boolean passwordIsValid = passwordEncoder.matches(loginRequest.senha(), cliente.getSenha());

        if (!passwordIsValid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha ou usuário incorreto");
        }

        return jwtUtil.generateToken(cliente);
    }
}
