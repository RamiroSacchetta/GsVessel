package com.example.GSVessel.Controller;

import com.example.GSVessel.Model.Ship;
import com.example.GSVessel.Service.ShipService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/ships")
public class ShipController {

    private final ShipService shipService;

    public ShipController(ShipService shipService) {
        this.shipService = shipService;
    }

    // Listar todos los ships
    @GetMapping
    public List<Ship> getAllShips() {
        return shipService.getAllShips();
    }

    // Obtener ship por id
    @GetMapping("/{id}")
    public ResponseEntity<Ship> getShipById(@PathVariable Long id) {
        Ship ship = shipService.getShipById(id);
        return ResponseEntity.ok(ship);
    }

    // Crear ship enviando barcoNombre y userEmail como parámetros
    @PostMapping
    public Ship createShip(@RequestBody Ship ship,
                           @RequestParam String barcoNombre,
                           @RequestParam String userEmail) {
        return shipService.createShip(ship, barcoNombre, userEmail);
    }

    // Actualizar ship
    @PutMapping("/{id}")
    public Ship updateShip(@PathVariable Long id, @RequestBody Ship ship) {
        return shipService.updateShip(id, ship);
    }

    // Eliminar ship
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShip(@PathVariable Long id) {
        shipService.deleteShip(id);
        return ResponseEntity.noContent().build();
    }
}
