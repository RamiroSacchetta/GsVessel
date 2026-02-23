package com.example.GSVessel.Controller;

import com.example.GSVessel.DTO.EquipmentDTO;
import com.example.GSVessel.Model.Enums.EquipmentCategory;
import com.example.GSVessel.Model.Enums.EquipmentLocation;
import com.example.GSVessel.Service.EquipmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @GetMapping
    public ResponseEntity<List<EquipmentDTO>> getAllEquipment() {
        return ResponseEntity.ok(equipmentService.getAllEquipment());
    }

    @PostMapping
    public ResponseEntity<EquipmentDTO> createEquipment(
            @RequestParam("name") String name,
            @RequestParam("category") String category,
            @RequestParam("location") String location,
            @RequestParam("consumption") Double consumption,
            @RequestParam("hoursUsed") Integer hoursUsed,
            @RequestParam(value = "budget", required = false) Double budget,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("shipId") Long shipId,
            @RequestParam(value = "parentId", required = false) Long parentId,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        EquipmentDTO dto = new EquipmentDTO();
        dto.setName(name);
        dto.setCategory(EquipmentCategory.valueOf(category));
        dto.setLocation(EquipmentLocation.valueOf(location));
        dto.setConsumption(consumption);
        dto.setHoursUsed(hoursUsed);
        dto.setBudget(budget);
        dto.setDescription(description);
        dto.setShipId(shipId);
        dto.setParentId(parentId);
        dto.setImage(image);

        EquipmentDTO saved = equipmentService.saveEquipment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipmentDTO> updateEquipment(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("category") String category,
            @RequestParam("location") String location,
            @RequestParam("consumption") Double consumption,
            @RequestParam("hoursUsed") Integer hoursUsed,
            @RequestParam(value = "budget", required = false) Double budget,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("shipId") Long shipId,
            @RequestParam(value = "parentId", required = false) Long parentId,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        EquipmentDTO dto = new EquipmentDTO();
        dto.setId(id);
        dto.setName(name);
        dto.setCategory(EquipmentCategory.valueOf(category));
        dto.setLocation(EquipmentLocation.valueOf(location));
        dto.setConsumption(consumption);
        dto.setHoursUsed(hoursUsed);
        dto.setBudget(budget);
        dto.setDescription(description);
        dto.setShipId(shipId);
        dto.setParentId(parentId);
        dto.setImage(image);

        EquipmentDTO updated = equipmentService.updateEquipment(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEquipment(@PathVariable Long id) {
        equipmentService.deleteEquipment(id);
        return ResponseEntity.ok(Map.of("message", "Equipo eliminado con éxito"));
    }

    @GetMapping("/ship/{shipId}")
    public ResponseEntity<List<EquipmentDTO>> getEquipmentByShip(@PathVariable Long shipId) {
        return ResponseEntity.ok(equipmentService.getEquipmentByShip(shipId));
    }

    @GetMapping("/tree/ship/{shipId}")
    public ResponseEntity<List<EquipmentDTO>> getEquipmentTreeByShip(@PathVariable Long shipId) {
        return ResponseEntity.ok(equipmentService.getEquipmentTreeByShip(shipId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentDTO> getEquipmentById(@PathVariable Long id) {
        return ResponseEntity.ok(equipmentService.getEquipmentById(id));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<EquipmentDTO>> getEquipmentByCategory(@PathVariable EquipmentCategory category) {
        return ResponseEntity.ok(equipmentService.getEquipmentByCategory(category));
    }

    // 🔁 Endpoint para reiniciar horas (usado tras mantenimiento de reemplazo)
    @PostMapping("/{id}/reset-hours")
    public ResponseEntity<EquipmentDTO> resetHours(@PathVariable Long id) {
        EquipmentDTO dto = equipmentService.resetHours(id);
        return ResponseEntity.ok(dto);
    }
}