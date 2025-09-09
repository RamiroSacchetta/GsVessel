package com.example.GSVessel.Controller;

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


        @PostMapping
        public ResponseEntity<Maintenance> create(@RequestBody Maintenance maintenance) {
            return ResponseEntity.ok(maintenanceService.create(maintenance));
        }

        @GetMapping
        public ResponseEntity<List<Maintenance>> getAll() {
            return ResponseEntity.ok(maintenanceService.findAll());
        }

        @GetMapping("/{id}")
        public ResponseEntity<Maintenance> getById(@PathVariable Long id) {
            return ResponseEntity.ok(maintenanceService.findById(id));
        }

        @GetMapping("/tipo/{tipo}")
        public ResponseEntity<List<Maintenance>> getByTipo(@PathVariable TipoMaintenance tipo) {
            return ResponseEntity.ok(maintenanceService.findByTipo(tipo));
        }

        @PutMapping("/{id}")
        public ResponseEntity<Maintenance> update(@PathVariable Long id, @RequestBody Maintenance maintenance) {
            return ResponseEntity.ok(maintenanceService.update(id, maintenance));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> delete(@PathVariable Long id) {
            maintenanceService.delete(id);
            return ResponseEntity.noContent().build();
        }

}
