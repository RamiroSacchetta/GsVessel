package com.example.GSVessel.Controller;

import com.example.GSVessel.DTO.MaintenanceDTO;
import com.example.GSVessel.Model.Enums.TipoMaintenance;
import com.example.GSVessel.Service.MaintenanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceController {

    private static final Logger logger = LoggerFactory.getLogger(MaintenanceController.class);

    private final MaintenanceService maintenanceService;

    public MaintenanceController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    // Crear mantenimiento (con imagen opcional) usando @RequestParam
    @PostMapping
    public ResponseEntity<MaintenanceDTO> create(
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String fechaStr,
            @RequestParam("costo") String costoStr,
            @RequestParam(value = "descripcion", required = false) String descripcion,
            @RequestParam("tipoMaintenance") String tipoMaintenanceStr,
            @RequestParam("equipmentId") Long equipmentId,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        logger.info("Recibiendo parametros para creación (raw): FechaStr={}, CostoStr={}, Descripcion={}, TipoStr={}, EquipmentId={}, ImagePresent={}",
                fechaStr, costoStr, descripcion, tipoMaintenanceStr, equipmentId, image != null);

        // Convertir fecha
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(fechaStr);
        } catch (Exception e) {
            logger.error("Error al parsear la fecha: {}", fechaStr, e);
            return ResponseEntity.badRequest().build();
        }

        // Convertir costo
        BigDecimal costo;
        try {
            costo = new BigDecimal(costoStr);
        } catch (Exception e) {
            logger.error("Error al parsear el costo: {}", costoStr, e);
            return ResponseEntity.badRequest().build();
        }

        // Convertir tipo
        TipoMaintenance tipoMaintenance;
        try {
            tipoMaintenance = TipoMaintenance.valueOf(tipoMaintenanceStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.error("Tipo de mantenimiento inválido recibido: {}", tipoMaintenanceStr, e);
            return ResponseEntity.badRequest().build();
        }

        logger.info("Recibiendo parametros para creación (parsed): Fecha={}, Costo={}, Descripcion={}, Tipo={}, EquipmentId={}, ImagePresent={}",
                fecha, costo, descripcion, tipoMaintenance, equipmentId, image != null);

        MaintenanceDTO dto = MaintenanceDTO.builder()
                .fecha(fecha)
                .costo(costo)
                .descripcion(descripcion)
                .tipoMaintenance(tipoMaintenance)
                .equipmentId(equipmentId)
                .image(image)
                .build();

        try {
            MaintenanceDTO saved = maintenanceService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            logger.error("Error al crear mantenimiento en el servicio", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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

    // Actualizar mantenimiento (con imagen opcional) - USANDO @RequestParam
    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceDTO> update(
            @PathVariable Long id,
            @RequestParam(value = "fecha", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String fechaStr,
            @RequestParam(value = "costo", required = false) String costoStr, // Ahora puede ser null
            @RequestParam(value = "descripcion", required = false) String descripcion,
            @RequestParam(value = "tipoMaintenance", required = false) String tipoMaintenanceStr, // Ahora puede ser null
            @RequestParam(value = "equipmentId", required = false) Long equipmentId, // Ahora puede ser null
            @RequestParam(value = "taller", required = false) String taller, // Nuevo campo
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        logger.info("Recibiendo parametros para actualización (raw): Id={}, FechaStr={}, CostoStr={}, Descripcion={}, TipoStr={}, EquipmentId={}, Taller={}, ImagePresent={}",
                id, fechaStr, costoStr, descripcion, tipoMaintenanceStr, equipmentId, taller, image != null);

        // Convertir fecha si se envió
        LocalDate fecha = null;
        if (fechaStr != null && !fechaStr.trim().isEmpty()) {
            try {
                fecha = LocalDate.parse(fechaStr);
            } catch (Exception e) {
                logger.error("Error al parsear la fecha para update: {}", fechaStr, e);
                return ResponseEntity.badRequest().build();
            }
        }

        // Convertir costo si se envió
        BigDecimal costo = null;
        if (costoStr != null && !costoStr.trim().isEmpty()) {
            try {
                costo = new BigDecimal(costoStr);
            } catch (Exception e) {
                logger.error("Error al parsear el costo para update: {}", costoStr, e);
                return ResponseEntity.badRequest().build();
            }
        }

        // Convertir tipo si se envió
        TipoMaintenance tipoMaintenance = null;
        if (tipoMaintenanceStr != null && !tipoMaintenanceStr.trim().isEmpty()) {
            try {
                tipoMaintenance = TipoMaintenance.valueOf(tipoMaintenanceStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                logger.error("Tipo de mantenimiento inválido recibido para update: {}", tipoMaintenanceStr, e);
                return ResponseEntity.badRequest().build();
            }
        }

        // Construye DTO de actualización
        MaintenanceDTO dto = MaintenanceDTO.builder()
                .fecha(fecha)
                .costo(costo)
                .descripcion(descripcion)
                .tipoMaintenance(tipoMaintenance)
                .equipmentId(equipmentId)
                .taller(taller) // Nuevo campo
                .image(image)
                .build();

        try {
            MaintenanceDTO updated = maintenanceService.update(id, dto);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            logger.error("Error al actualizar mantenimiento en el servicio", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Eliminar mantenimiento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        maintenanceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}