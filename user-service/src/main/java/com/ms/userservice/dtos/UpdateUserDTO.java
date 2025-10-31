package com.ms.userservice.dtos;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserDTO(
        @Size(min = 3, max = 30, message = "O nome de usuário deve ter entre 3 e 30 caracteres.")
        String userName,

        @Email(message = "O email deve ser válido.")
        String email,

        @Size(min = 6, message = "A nova senha deve ter no mínimo 6 caracteres.")
        String password,

        String fullName,

        String country
) {}