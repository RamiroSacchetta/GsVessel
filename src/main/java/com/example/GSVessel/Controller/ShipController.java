package com.example.GSVessel.Controller;

import com.example.GSVessel.DTO.ShipDTO;
import com.example.GSVessel.Model.Ship;
import com.example.GSVessel.Service.ShipService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/ships")
public class ShipController {

    private final ShipService shipService;

    public ShipController(ShipService shipService) {
        this.shipService = shipService;
    }

    // Listar ships del usuario autenticado (owner scope)
    @GetMapping
    public List<Ship> getAllShips(Authentication authentication) {
        String email = authentication.getName();
        return shipService.getAllShips(email);
    }

    // Obtener ship por id solo si pertenece al usuario autenticado
    @GetMapping("/{id}")
    public ResponseEntity<Ship> getShipById(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        Ship ship = shipService.getShipById(id, email);
        return ResponseEntity.ok(ship);
    }

    // Crear ship asociado a un barco del usuario autenticado
    @PostMapping
    public Ship createShip(@RequestBody ShipDTO shipDTO, Authentication authentication) {
        String userEmail = authentication.getName();
        return shipService.createShip(shipDTO, userEmail);
    }

    // Actualizar ship solo si pertenece al usuario autenticado
    @PutMapping("/{id}")
    public Ship updateShip(@PathVariable Long id, @RequestBody Ship ship, Authentication authentication) {
        String email = authentication.getName();
        return shipService.updateShip(id, ship, email);
    }

    // Eliminar ship solo si pertenece al usuario autenticado
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShip(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        shipService.deleteShip(id, email);
        return ResponseEntity.noContent().build();
    }
}
