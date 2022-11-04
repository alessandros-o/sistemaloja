package com.alessandro.sistemaloja.dto;

import java.util.List;

public record AuthenticatedUserDetails(Integer id, String email, List<String> perfis) {
}
