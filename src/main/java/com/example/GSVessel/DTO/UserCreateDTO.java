package com.example.GSVessel.DTO;

import com.example.GSVessel.Model.Enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateDTO(
        @NotBlank(message = "Username es obligatorio")
        @Size(min = 3, max = 50, message = "Username debe tener entre 3 y 50 caracteres")
        String username,

        @NotBlank(message = "Email es obligatorio")
        @Email(message = "Email debe ser válido")
        String email,

        @NotBlank(message = "Password es obligatorio")
        @Size(min = 6, message = "Password debe tener al menos 6 caracteres")
        String password,

        Role role // opcional, si no se envía se asigna USER por defecto
) {}
