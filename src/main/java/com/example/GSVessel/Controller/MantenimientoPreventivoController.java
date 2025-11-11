package com.example.GSVessel.Controller;

import com.example.GSVessel.DTO.MantenimientoPreventivoDTO;
import com.example.GSVessel.Service.MantenimientoPreventivoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/preventive")
public class MantenimientoPreventivoController {

    private final MantenimientoPreventivoService mantenimientoPreventivoService;

    public MantenimientoPreventivoController(MantenimientoPreventivoService mantenimientoPreventivoService) {
        this.mantenimientoPreventivoService = mantenimientoPreventivoService;
    }

    // Crear nuevo mantenimiento preventivo
    @PostMapping
    public ResponseEntity<MantenimientoPreventivoDTO> createMantenimientoPreventivo(@RequestBody MantenimientoPreventivoDTO dto) {
        MantenimientoPreventivoDTO saved = mantenimientoPreventivoService.saveMantenimientoPreventivo(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Actualizar mantenimiento preventivo
    @PutMapping("/{id}")
    public ResponseEntity<MantenimientoPreventivoDTO> updateMantenimientoPreventivo(
            @PathVariable Long id,
            @RequestBody MantenimientoPreventivoDTO dto) {
        MantenimientoPreventivoDTO updated = mantenimientoPreventivoService.updateMantenimientoPreventivo(id, dto);
        return ResponseEntity.ok(updated);
    }

    // Obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<MantenimientoPreventivoDTO> getMantenimientoPreventivoById(@PathVariable Long id) {
        MantenimientoPreventivoDTO dto = mantenimientoPreventivoService.getMantenimientoPreventivoById(id);
        return ResponseEntity.ok(dto);
    }

    // Listar todos
    @GetMapping
    public ResponseEntity<List<MantenimientoPreventivoDTO>> getAllMantenimientosPreventivos() {
        List<MantenimientoPreventivoDTO> dtos = mantenimientoPreventivoService.getAllMantenimientosPreventivos();
        return ResponseEntity.ok(dtos);
    }

    // Listar por ID del equipo
    @GetMapping("/equipment/{equipmentId}")
    public ResponseEntity<List<MantenimientoPreventivoDTO>> getByEquipmentId(@PathVariable Long equipmentId) {
        List<MantenimientoPreventivoDTO> dtos = mantenimientoPreventivoService.getByEquipmentId(equipmentId);
        return ResponseEntity.ok(dtos);
    }

    // Listar por ID del barco
    @GetMapping("/ship/{shipId}")
    public ResponseEntity<List<MantenimientoPreventivoDTO>> getByShipId(@PathVariable Long shipId) {
        List<MantenimientoPreventivoDTO> dtos = mantenimientoPreventivoService.getByShipId(shipId);
        return ResponseEntity.ok(dtos);
    }

    // Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMantenimientoPreventivo(@PathVariable Long id) {
        mantenimientoPreventivoService.deleteMantenimientoPreventivo(id);
        return ResponseEntity.ok(Map.of("message", "Mantenimiento Preventivo eliminado con éxito"));
    }

    // Reiniciar horas de uso del equipo asociado y actualizar fecha
    @PutMapping("/{id}/reiniciar-horas")
    public ResponseEntity<MantenimientoPreventivoDTO> reiniciarHoras(@PathVariable Long id) {
        MantenimientoPreventivoDTO updated = mantenimientoPreventivoService.reiniciarHoras(id);
        return ResponseEntity.ok(updated);
    }

    // Sumar horas al equipo (sin necesidad de modificar el mantenimiento preventivo)
    @PutMapping("/equipment/{equipmentId}/sumar-horas")
    public ResponseEntity<?> sumarHoras(
            @PathVariable Long equipmentId,
            @RequestParam int horas) {
        mantenimientoPreventivoService.sumarHorasAEquipo(equipmentId, horas);
        return ResponseEntity.ok(Map.of("message", "Horas sumadas correctamente"));
    }
}