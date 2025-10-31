package com.ms.userservice.dtos;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserDTO(
        @NotBlank(message = "O nome de usuário é obrigatório.")
        @Size(min = 3, max = 30, message = "O nome de usuário deve ter entre 3 e 30 caracteres.")
        String userName,

        @NotBlank(message = "O email é obrigatório.")
        @Email(message = "O email deve ser válido.")
        String email,

        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
        String password,

        @NotBlank(message = "O nome completo é obrigatório.")
        String fullName,

        @NotBlank(message = "O país é obrigatório.")
        String country
) {}