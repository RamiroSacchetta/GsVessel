package com.example.GSVessel.Controller;

import com.example.GSVessel.Model.User;
import com.example.GSVessel.Model.DTO.UserDTO;
import com.example.GSVessel.Model.Enums.Role;
import com.example.GSVessel.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers()
                .stream()
                .map(u -> new UserDTO(u.getId(), u.getUsername(), u.getEmail(), Role.valueOf(String.valueOf(u.getRole()))))
                .collect(Collectors.toList());

        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(users);
    }

    // Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        User u = userService.getUserById(id);
        UserDTO dto = new UserDTO(u.getId(), u.getUsername(), u.getEmail(), Role.valueOf(String.valueOf(u.getRole())));
        return ResponseEntity.ok(dto);
    }

    // Crear nuevo usuario
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        UserDTO dto = new UserDTO(createdUser.getId(), createdUser.getUsername(), createdUser.getEmail(),
                Role.valueOf(String.valueOf(createdUser.getRole())));
        return ResponseEntity.ok(dto);
    }

    // Actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        UserDTO dto = new UserDTO(updatedUser.getId(), updatedUser.getUsername(), updatedUser.getEmail(),
                Role.valueOf(String.valueOf(updatedUser.getRole())));
        return ResponseEntity.ok(dto);
    }

    // Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
