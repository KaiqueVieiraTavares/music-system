package com.ms.userservice.dtos;

import com.ms.userservice.model.enums.Role;

import java.util.UUID;

public record AuthResponseDto(UUID userID, String email, Role role, String token) {
}
