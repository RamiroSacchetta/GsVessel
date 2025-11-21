package com.example.GSVessel.Controller;

import com.example.GSVessel.DTO.EquipmentDTO;
import com.example.GSVessel.Model.Enums.EquipmentCategory;
import com.example.GSVessel.Model.Enums.EquipmentLocation;
import com.example.GSVessel.Service.EquipmentService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
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
    private final Cloudinary cloudinary; // Inyecta Cloudinary

    public EquipmentController(EquipmentService equipmentService, Cloudinary cloudinary) {
        this.equipmentService = equipmentService;
        this.cloudinary = cloudinary;
    }

    // Listar todos los equipos
    @GetMapping
    public ResponseEntity<List<EquipmentDTO>> getAllEquipment() {
        List<EquipmentDTO> equipments = equipmentService.getAllEquipment();
        return ResponseEntity.ok(equipments);
    }

    // Crear nuevo equipo (con imagen opcional)
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
            @RequestParam(value = "image", required = false) MultipartFile image) throws Exception {

        EquipmentDTO equipmentDTO = new EquipmentDTO();
        equipmentDTO.setName(name);
        equipmentDTO.setCategory(EquipmentCategory.valueOf(category));
        equipmentDTO.setLocation(EquipmentLocation.valueOf(location));
        equipmentDTO.setConsumption(consumption);
        equipmentDTO.setHoursUsed(hoursUsed);
        equipmentDTO.setBudget(budget);
        equipmentDTO.setDescription(description);
        equipmentDTO.setShipId(shipId);
        equipmentDTO.setImage(image); // Asigna la imagen al DTO

        EquipmentDTO saved = equipmentService.saveEquipment(equipmentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Actualizar equipo (con imagen opcional)
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
            @RequestParam(value = "image", required = false) MultipartFile image) throws Exception {

        EquipmentDTO equipmentDTO = new EquipmentDTO();
        equipmentDTO.setId(id);
        equipmentDTO.setName(name);
        equipmentDTO.setCategory(EquipmentCategory.valueOf(category));
        equipmentDTO.setLocation(EquipmentLocation.valueOf(location));
        equipmentDTO.setConsumption(consumption);
        equipmentDTO.setHoursUsed(hoursUsed);
        equipmentDTO.setBudget(budget);
        equipmentDTO.setDescription(description);
        equipmentDTO.setShipId(shipId);
        equipmentDTO.setImage(image); // Asigna la imagen al DTO

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