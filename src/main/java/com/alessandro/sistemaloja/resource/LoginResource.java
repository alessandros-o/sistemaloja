package com.alessandro.sistemaloja.resource;

import com.alessandro.sistemaloja.dto.LoginRequest;
import com.alessandro.sistemaloja.dto.TokenResponse;
import com.alessandro.sistemaloja.service.LoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/login")
public class LoginResource {

    private final LoginService loginService;

    public LoginResource(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public TokenResponse login(@RequestBody @Valid LoginRequest loginRequest) {
        return new TokenResponse(loginService.login(loginRequest));
    }
}
