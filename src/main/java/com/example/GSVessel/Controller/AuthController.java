package com.example.GSVessel.Controller;

import com.example.GSVessel.DTO.RegisterUserDTO;
import com.example.GSVessel.DTO.UserLoginDTO;
import com.example.GSVessel.Model.User;
import com.example.GSVessel.Service.UserService;
import com.example.GSVessel.Security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(UserService userService,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    // ✅ Registro seguro con DTO
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody com.example.GSVessel.DTO.RegisterUserDTO dto) {
        User user = new User();
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        // El rol se asigna como USER en el servicio

        userService.registerUser(user);
        return ResponseEntity.ok("Usuario registrado. Revisa tu email para confirmar la cuenta.");
    }

    // ✅ Confirmación con manejo básico de errores
    @GetMapping("/confirm")
    public ResponseEntity<String> confirm(@RequestParam String token) {
        try {
            String result = userService.confirmUser(token);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ✅ Login con JWT
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserLoginDTO request) {
        try {
            // Asegúrate de que el primer parámetro sea el "username" que espera Spring Security
            // Si usas email como username, envía request.getEmail()
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            String jwt = jwtUtil.generateToken(authentication.getName());
            return ResponseEntity.ok(jwt);

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }
}