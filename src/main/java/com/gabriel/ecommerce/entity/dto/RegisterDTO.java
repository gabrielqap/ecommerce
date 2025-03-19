package com.gabriel.ecommerce.entity.dto;

import com.gabriel.ecommerce.enums.Role;

public record RegisterDTO(String username, String password, String email, Role role) {
}
