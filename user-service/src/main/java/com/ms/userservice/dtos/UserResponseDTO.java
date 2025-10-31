package com.ms.userservice.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String userName,
        String email,
        String fullName,
        String country,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}