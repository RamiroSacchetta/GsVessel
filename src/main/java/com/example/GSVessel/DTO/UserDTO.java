package com.example.GSVessel.Model.DTO;

import com.example.GSVessel.Model.Enums.Role;

public record UserDTO(Long id, String username, String email, Role role) {}
