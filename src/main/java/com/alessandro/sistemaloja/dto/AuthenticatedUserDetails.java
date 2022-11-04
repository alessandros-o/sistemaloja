package com.alessandro.sistemaloja.dto;

import java.util.List;

public record AuthenticatedUserDetails(Long id, String email, List<String> perfis) {
}
