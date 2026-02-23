package com.example.GSVessel.Controller;

import com.example.GSVessel.Model.Barco;
import com.example.GSVessel.Service.BarcoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/barcos")
public class BarcoController {

    private final BarcoService barcoService;

    public BarcoController(BarcoService barcoService) {
        this.barcoService = barcoService;
    }

    // Listar barcos del usuario autenticado
    @GetMapping
    public List<Barco> getAllBarcos(Authentication authentication) {
        String email = authentication.getName();
        return barcoService.getAllBarcos(email);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Barco> getBarcoById(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        Barco barco = barcoService.getBarcoById(id, email);
        return ResponseEntity.ok(barco);
    }

    // Crear barco asignando owner por token
    @PostMapping
    public Barco createBarco(@RequestBody Barco barco, Authentication authentication) {
        String email = authentication.getName();
        return barcoService.createBarco(barco, email);
    }

    // Actualizar solo si pertenece al usuario autenticado
    @PutMapping("/{id}")
    public Barco updateBarco(@PathVariable Long id, @RequestBody Barco barco, Authentication authentication) {
        String email = authentication.getName();
        return barcoService.updateBarco(id, barco, email);
    }

    // Eliminar solo si pertenece al usuario autenticado
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBarco(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        barcoService.deleteBarco(id, email);
        return ResponseEntity.noContent().build();
    }

    // Recomendado: quitar este endpoint o restringirlo a ADMIN.
    @GetMapping("/user/{userId}")
    public List<Barco> getBarcosByUser(@PathVariable Long userId) {
        return barcoService.getBarcosByUser(userId);
    }
}
