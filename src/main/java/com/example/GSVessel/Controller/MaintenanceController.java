package com.example.GSVessel.Controller;

import com.example.GSVessel.DTO.MaintenanceDTO;
import com.example.GSVessel.Mapper.MaintenanceMapper;
import com.example.GSVessel.Model.Enums.TipoMaintenance;
import com.example.GSVessel.Model.Maintenance;
import com.example.GSVessel.Service.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceController {

    @Autowired
    private MaintenanceService maintenanceService;

    // Crear mantenimiento
    @PostMapping
    public ResponseEntity<MaintenanceDTO> create(@RequestBody MaintenanceDTO maintenanceDTO) {
        MaintenanceDTO saved = maintenanceService.create(maintenanceDTO);
        return ResponseEntity.status(201).body(saved);
    }

    // Listar todos los mantenimientos
    @GetMapping
    public ResponseEntity<List<MaintenanceDTO>> getAll() {
        return ResponseEntity.ok(maintenanceService.findAll());
    }

    // Obtener mantenimiento por ID
    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(maintenanceService.findById(id));
    }

    // Obtener mantenimientos por tipo
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<MaintenanceDTO>> getByTipo(@PathVariable TipoMaintenance tipo) {
        return ResponseEntity.ok(maintenanceService.findByTipo(tipo));
    }

    // Actualizar mantenimiento
    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceDTO> update(@PathVariable Long id, @RequestBody MaintenanceDTO dto) {
        return ResponseEntity.ok(maintenanceService.update(id, dto));
    }

    // Eliminar mantenimiento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        maintenanceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
