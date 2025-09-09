package com.example.GSVessel.Controller;

import com.example.GSVessel.Model.User;
import com.example.GSVessel.Service.UserService;
import com.example.GSVessel.Security.JwtUtil;
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

    // Registro de usuario
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        userService.registerUser(user);
        return ResponseEntity.ok("Usuario registrado. Revisa tu email para confirmar la cuenta.");
    }

    // Confirmar cuenta con token
    @GetMapping("/confirm")
    public ResponseEntity<String> confirm(@RequestParam String token) {
        String result = userService.confirmUser(token);
        return ResponseEntity.ok(result);
    }

    // Login con JWT
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username,
                                        @RequestParam String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // Generar token JWT
            String jwt = jwtUtil.generateToken(authentication.getName());
            return ResponseEntity.ok(jwt);

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }
}
