package com.example.GSVessel.Model.DTO;

import com.example.GSVessel.Model.Enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateDTO(
        @Size(min = 3, max = 50, message = "Username debe tener entre 3 y 50 caracteres")
        String username,

        @Email(message = "Email debe ser válido")
        String email,

        @Size(min = 6, message = "Password debe tener al menos 6 caracteres")
        String password,

        Role role
) {}
