package com.example.GSVessel.Controller;

import com.example.GSVessel.Model.Equipment;
import com.example.GSVessel.Model.Enums.EquipmentCategory;
import com.example.GSVessel.Service.EquipmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    // Listar todos los equipos
    @GetMapping
    public List<Equipment> getAllEquipment() {
        return equipmentService.getAllEquipment();
    }

    // Buscar equipo por ID
    @GetMapping("/{id}")
    public ResponseEntity<Equipment> getEquipmentById(@PathVariable Long id) {
        Optional<Equipment> equipment = equipmentService.getEquipmentById(id);
        return equipment.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear nuevo equipo
    @PostMapping
    public ResponseEntity<Equipment> createEquipment(@RequestBody Equipment equipment) {
        Equipment saved = equipmentService.saveEquipment(equipment);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // Actualizar equipo
    @PutMapping("/{id}")
    public ResponseEntity<Equipment> updateEquipment(@PathVariable Long id, @RequestBody Equipment equipment) {
        Optional<Equipment> existing = equipmentService.getEquipmentById(id);
        if (existing.isPresent()) {
            equipment.setId(id);
            Equipment updated = equipmentService.updateEquipment(equipment);
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar equipo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {
        Optional<Equipment> existing = equipmentService.getEquipmentById(id);
        if (existing.isPresent()) {
            equipmentService.deleteEquipment(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Filtrar por barco
    @GetMapping("/ship/{shipId}")
    public List<Equipment> getEquipmentByShip(@PathVariable Long shipId) {
        return equipmentService.getEquipmentByShip(shipId);
    }

    // Filtrar por categoría
    @GetMapping("/category/{category}")
    public List<Equipment> getEquipmentByCategory(@PathVariable EquipmentCategory category) {
        return equipmentService.getEquipmentByCategory(category);
    }
}

