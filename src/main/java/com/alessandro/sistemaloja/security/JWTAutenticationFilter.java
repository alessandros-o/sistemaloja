package com.alessandro.sistemaloja.security;

import com.alessandro.sistemaloja.dto.AuthenticatedUserDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class JWTAutenticationFilter extends OncePerRequestFilter {

    private static final String BEAR = "Bearer ";
    private JWTUtil jwtUtil;

    public JWTAutenticationFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (Objects.isNull(authorizationHeader) || !authorizationHeader.startsWith(BEAR)) {
            filterChain.doFilter(request, response);
            return;
        }

        AuthenticatedUserDetails authenticatedUserDetails = jwtUtil.validateAndGetUser(authorizationHeader.replace(BEAR, ""));

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                authenticatedUserDetails,
                null,
                authenticatedUserDetails.perfis().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

        response.addHeader("access-control-expose-headers", "Authorization");
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }

}
