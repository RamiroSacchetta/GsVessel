package com.example.GSVessel.Exception;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsernameDuplicate extends RuntimeException {
    public UsernameDuplicate(String message, @NotBlank(message = "El username no puede estar vacío") @Size(min = 3, max = 50, message = "El username debe tener entre 3 y 50 caracteres") String username) {
        super(message);
    }
}
