package com.example.GSVessel.Controller;

import com.example.GSVessel.Model.Enums.Role;
import com.example.GSVessel.Model.User;
import com.example.GSVessel.DTO.RegisterUserDTO;
import com.example.GSVessel.DTO.UserDTO;
import com.example.GSVessel.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
                .map(u -> new UserDTO(u.getId(), u.getUsername(), u.getEmail(), u.getRole()))
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
        UserDTO dto = new UserDTO(u.getId(), u.getUsername(), u.getEmail(), u.getRole());
        return ResponseEntity.ok(dto);
    }

    // Registro público (sin rol en la entrada)
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody RegisterUserDTO dto) {
        User user = new User();
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        // El rol se asigna automáticamente como USER en el servicio

        User createdUser = userService.registerUser(user);
        UserDTO responseDto = new UserDTO(
                createdUser.getId(),
                createdUser.getUsername(),
                createdUser.getEmail(),
                createdUser.getRole()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // Actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        UserDTO dto = new UserDTO(updatedUser.getId(), updatedUser.getUsername(),
                updatedUser.getEmail(), updatedUser.getRole());
        return ResponseEntity.ok(dto);
    }

    // Eliminar usuario por id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    //eliminar por mail
    @DeleteMapping ("/delete/{email}")
    public ResponseEntity<Void> deleteUserByEmail(@PathVariable String email) {
        userService.deleteByEmail(email);
        return ResponseEntity.noContent().build();
    }

    // Manejo de errores de validación
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }


    @GetMapping("/miPerfil")
    public ResponseEntity<UserDTO> miPerfil(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String loginName = authentication.getName();
        return ResponseEntity.ok(userService.getUserByLoginName(loginName));

    }

    @PostMapping("/registerEmployee")
    public ResponseEntity<UserDTO> registerEmployee(
            Authentication auth,
            @Valid @RequestBody RegisterUserDTO dto
    ) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // usuario logueado (owner/admin) —
        User creator = userService.findEntityByLoginName(auth.getName());

        User created = userService.registerEmployeeForOwner(dto, creator);

        // mapear a DTO explícito para evitar ciclos y exponer solo lo necesario
        UserDTO response = new UserDTO(created.getId(), created.getUsername(), created.getEmail(), created.getRole());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/empleados")
    public ResponseEntity<List<UserDTO>> getEmpleadosDelOwner(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String caller = authentication.getName();
        User callerUser = userService.findEntityByLoginName(caller); // ya lo tenés en el service

        // Sólo permití OWNER o ADMIN (si querés)
        if (!(callerUser.getRole() == Role.OWNER || callerUser.getRole() == Role.ADMIN)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<UserDTO> empleados = userService.getEmployeesByOwnerId(callerUser.getId());
        if (empleados.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(empleados);
    }

}