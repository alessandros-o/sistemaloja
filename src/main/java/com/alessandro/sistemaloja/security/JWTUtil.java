package com.alessandro.sistemaloja.security;

import com.alessandro.sistemaloja.domain.Cliente;
import com.alessandro.sistemaloja.domain.enums.Perfil;
import com.alessandro.sistemaloja.dto.AuthenticatedUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JWTUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(Cliente cliente) {
        return Jwts.builder()
                .setSubject(cliente.getId().toString())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .claim("perfis", cliente.getPerfis().stream().map(Perfil::getDescricao).collect(Collectors.toList()))
                .signWith(getSecretKey())
                .compact();
    }

    public AuthenticatedUserDetails validateAndGetUser(String jwtToken) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSecretKey()).build()
                .parseClaimsJws(jwtToken).getBody();

        return new AuthenticatedUserDetails(
                Integer.valueOf(claims.getSubject()),
                claims.get("email", String.class),
                claims.get("perfis", List.class)
        );
    }

    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
