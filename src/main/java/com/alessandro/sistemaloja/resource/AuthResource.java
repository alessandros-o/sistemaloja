package com.alessandro.sistemaloja.resource;

import com.alessandro.sistemaloja.domain.Cliente;
import com.alessandro.sistemaloja.dto.AuthenticatedUserDetails;
import com.alessandro.sistemaloja.dto.EmailDto;
import com.alessandro.sistemaloja.security.JWTUtil;
import com.alessandro.sistemaloja.service.AuthService;
import com.alessandro.sistemaloja.service.ClienteService;
import com.alessandro.sistemaloja.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private AuthService service;

    @PostMapping(value = "/refresh_token")
    public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
        AuthenticatedUserDetails user = UserDetailsService.authenticated();
        Cliente cliente = clienteService.buscarPorId(user.id());
        String token = jwtUtil.generateToken(cliente);
        response.addHeader("Authorization", "Bearer " + token);
        response.addHeader("access-control-expose-headers", "Authorization");
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/forgot")
    public ResponseEntity<Void> forgot(@Valid @RequestBody EmailDto emailDto) {
        service.sendNewPassword(emailDto.getEmail());
        return ResponseEntity.noContent().build();
    }


}
