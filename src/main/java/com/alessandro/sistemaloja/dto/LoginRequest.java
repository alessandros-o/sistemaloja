package com.alessandro.sistemaloja.dto;

import javax.validation.constraints.NotEmpty;

public record LoginRequest(@NotEmpty String email, @NotEmpty String senha) {
}
