package com.example.GSVessel.Controller;

import com.example.GSVessel.DTO.EquipmentDTO;
import com.example.GSVessel.Model.Enums.EquipmentCategory;
import com.example.GSVessel.Service.EquipmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    // Listar todos los equipos
    @GetMapping
    public ResponseEntity<List<EquipmentDTO>> getAllEquipment() {
        List<EquipmentDTO> equipments = equipmentService.getAllEquipment();
        return ResponseEntity.ok(equipments);
    }

    // Crear nuevo equipo (con imagen opcional)
    @PostMapping
    public ResponseEntity<EquipmentDTO> createEquipment(@ModelAttribute EquipmentDTO equipmentDTO) {
        EquipmentDTO saved = equipmentService.saveEquipment(equipmentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Actualizar equipo (con imagen opcional)
    @PutMapping("/{id}")
    public ResponseEntity<EquipmentDTO> updateEquipment(
            @PathVariable Long id,
            @ModelAttribute EquipmentDTO equipmentDTO) {
        EquipmentDTO updated = equipmentService.updateEquipment(id, equipmentDTO);
        return ResponseEntity.ok(updated);
    }

    // Eliminar equipo
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEquipment(@PathVariable Long id) {
        equipmentService.deleteEquipment(id);
        return ResponseEntity.ok(Map.of("message", "Equipo eliminado con éxito"));
    }

    // Filtrar por barco
    @GetMapping("/ship/{shipId}")
    public ResponseEntity<List<EquipmentDTO>> getEquipmentByShip(@PathVariable Long shipId) {
        List<EquipmentDTO> equipments = equipmentService.getEquipmentByShip(shipId);
        return ResponseEntity.ok(equipments);
    }

    // Filtrar por categoría
    @GetMapping("/category/{category}")
    public ResponseEntity<List<EquipmentDTO>> getEquipmentByCategory(@PathVariable EquipmentCategory category) {
        List<EquipmentDTO> equipments = equipmentService.getEquipmentByCategory(category);
        return ResponseEntity.ok(equipments);
    }
}