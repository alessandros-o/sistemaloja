package com.alessandro.sistemaloja.dto;

public class TokenResponse {

    private String token;

    public TokenResponse(String token) {
        this.token = token;
    }

    public TokenResponse() {
    }

    public String getToken() {
        return token;
    }
}
