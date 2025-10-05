package com.example.GSVessel.Controller;

import com.example.GSVessel.Model.Barco;
import com.example.GSVessel.Service.BarcoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/barcos")
public class BarcoController {

    private final BarcoService barcoService;

    public BarcoController(BarcoService barcoService) {
        this.barcoService = barcoService;
    }

    // Listar todos los barcos
    @GetMapping
    public List<Barco> getAllBarcos() {
        return barcoService.getAllBarcos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Barco> getBarcoById(@PathVariable Long id) {
        Barco barco = barcoService.getBarcoById(id);
        return ResponseEntity.ok(barco);
    }

    // Crear barco asignando un usuario dueño
    @PostMapping
    public Barco createBarco(@RequestBody Barco barco, Authentication authentication) {
        String email = authentication.getName(); // viene del token JWT
        return barcoService.createBarco(barco, email);
    }

    // Actualizar barco
    @PutMapping("/{id}")
    public Barco updateBarco(@PathVariable Long id, @RequestBody Barco barco) {
        return barcoService.updateBarco(id, barco);
    }

    // Eliminar barco
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBarco(@PathVariable Long id) {
        barcoService.deleteBarco(id);
        return ResponseEntity.noContent().build();
    }

    // Obtener barcos de un usuario
    @GetMapping("/user/{userId}")
    public List<Barco> getBarcosByUser(@PathVariable Long userId) {
        return barcoService.getBarcosByUser(userId);
    }
}
