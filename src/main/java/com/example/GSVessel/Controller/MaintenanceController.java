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

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceController {

    private static final Logger logger = LoggerFactory.getLogger(MaintenanceController.class);
    private final MaintenanceService maintenanceService;

    public MaintenanceController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    @PostMapping
    public ResponseEntity<MaintenanceDTO> create(
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String fechaStr,
            @RequestParam(value = "costo", required = false) String costoStr,
            @RequestParam(value = "descripcion", required = false) String descripcion,
            @RequestParam("tipoMaintenance") String tipoMaintenanceStr,
            @RequestParam("equipmentId") Long equipmentId,
            @RequestParam(value = "taller", required = false) String taller,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        LocalDate fecha;
        try { fecha = LocalDate.parse(fechaStr); }
        catch (Exception e) { return ResponseEntity.badRequest().build(); }

        BigDecimal costo = null;
        if (costoStr != null && !costoStr.trim().isEmpty()) {
            try { costo = new BigDecimal(costoStr); }
            catch (Exception e) { return ResponseEntity.badRequest().build(); }
        }

        TipoMaintenance tipoMaintenance;
        try { tipoMaintenance = TipoMaintenance.valueOf(tipoMaintenanceStr.toUpperCase()); }
        catch (Exception e) { return ResponseEntity.badRequest().build(); }

        MaintenanceDTO dto = MaintenanceDTO.builder()
                .fecha(fecha)
                .costo(costo)
                .descripcion(descripcion)
                .tipoMaintenance(tipoMaintenance)
                .equipmentId(equipmentId)
                .taller(taller)
                .image(image)
                .build();

        MaintenanceDTO saved = maintenanceService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<MaintenanceDTO>> getAll() {
        return ResponseEntity.ok(maintenanceService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(maintenanceService.findById(id));
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<MaintenanceDTO>> getByTipo(@PathVariable TipoMaintenance tipo) {
        return ResponseEntity.ok(maintenanceService.findByTipo(tipo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceDTO> update(
            @PathVariable Long id,
            @RequestParam(value = "fecha", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String fechaStr,
            @RequestParam(value = "costo", required = false) String costoStr,
            @RequestParam(value = "descripcion", required = false) String descripcion,
            @RequestParam(value = "tipoMaintenance", required = false) String tipoMaintenanceStr,
            @RequestParam(value = "equipmentId", required = false) Long equipmentId,
            @RequestParam(value = "taller", required = false) String taller,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        LocalDate fecha = null;
        if (fechaStr != null && !fechaStr.isEmpty()) fecha = LocalDate.parse(fechaStr);

        BigDecimal costo = null;
        if (costoStr != null && !costoStr.isEmpty()) costo = new BigDecimal(costoStr);

        TipoMaintenance tipoMaintenance = null;
        if (tipoMaintenanceStr != null && !tipoMaintenanceStr.isEmpty()) {
            tipoMaintenance = TipoMaintenance.valueOf(tipoMaintenanceStr.toUpperCase());
        }

        MaintenanceDTO dto = MaintenanceDTO.builder()
                .fecha(fecha)
                .costo(costo)
                .descripcion(descripcion)
                .tipoMaintenance(tipoMaintenance)
                .equipmentId(equipmentId)
                .taller(taller)
                .image(image)
                .build();

        MaintenanceDTO updated = maintenanceService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        maintenanceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
