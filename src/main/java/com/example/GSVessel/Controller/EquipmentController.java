package com.example.GSVessel.Controller;

import com.example.GSVessel.DTO.EquipmentDTO;
import com.example.GSVessel.Model.Equipment;
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
    public List<EquipmentDTO> getAllEquipment() {
        return equipmentService.getAllEquipment();
    }

    // Crear nuevo equipo
    @PostMapping
    public ResponseEntity<EquipmentDTO> createEquipment(@RequestBody EquipmentDTO equipmentDTO) {
        Equipment equipment = new Equipment();
        equipment.setName(equipmentDTO.getName());
        equipment.setCategory(equipmentDTO.getCategory());
        equipment.setLocation(equipmentDTO.getLocation());
        equipment.setConsumption(equipmentDTO.getConsumption());
        equipment.setHoursUsed(equipmentDTO.getHoursUsed());
        equipment.setBudget(equipmentDTO.getBudget());
        equipment.setDescription(equipmentDTO.getDescription());
        equipment.setImageUrl(equipmentDTO.getImageUrl());

        if (equipmentDTO.getShipId() != null) {
            equipmentService.getAllEquipment(); // opcional si quieres validar el barco
        }

        Equipment saved = equipmentService.saveEquipment(equipment);
        return new ResponseEntity<>(equipmentService.convertToDTO(saved), HttpStatus.CREATED);
    }

    // Actualizar equipo
    @PutMapping("/{id}")
    public ResponseEntity<EquipmentDTO> updateEquipment(@PathVariable Long id, @RequestBody EquipmentDTO equipmentDTO) {
        EquipmentDTO updated = equipmentService.updateEquipment(id, equipmentDTO);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    // Eliminar equipo
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEquipment(@PathVariable Long id) {
        equipmentService.deleteEquipment(id);
        return ResponseEntity.ok(Map.of("message", "Equipo eliminado"));
    }

    // Filtrar por barco
    @GetMapping("/ship/{shipId}")
    public List<EquipmentDTO> getEquipmentByShip(@PathVariable Long shipId) {
        return equipmentService.getEquipmentByShip(shipId);
    }

    // Filtrar por categoría
    @GetMapping("/category/{category}")
    public List<EquipmentDTO> getEquipmentByCategory(@PathVariable EquipmentCategory category) {
        return equipmentService.getEquipmentByCategory(category);
    }
}
