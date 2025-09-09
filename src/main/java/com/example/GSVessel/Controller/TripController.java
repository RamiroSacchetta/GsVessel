package com.example.GSVessel.Controller;

import com.example.GSVessel.DTO.TripDTO;
import com.example.GSVessel.Service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/trips")
public class TripController {

    private final TripService tripService;

    @Autowired
    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    // Obtener todos los viajes
    @GetMapping
    public ResponseEntity<List<TripDTO>> getAllTrips() {
        List<TripDTO> trips = tripService.getAllTrips();
        if (trips.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(trips);
    }

    // Obtener viaje por ID
    @GetMapping("/{id}")
    public ResponseEntity<TripDTO> getTripById(@PathVariable Long id) {
        Optional<TripDTO> trip = tripService.getTripById(id);
        return trip.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear viaje
    @PostMapping
    public ResponseEntity<?> createTrip(@RequestBody TripDTO tripDTO) {
        Optional<TripDTO> createdTrip = tripService.createTrip(tripDTO);
        if (createdTrip.isEmpty()) {
            return ResponseEntity.badRequest().body("El barco no existe");
        }
        return ResponseEntity.ok(createdTrip.get());
    }

    // Actualizar viaje
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTrip(@PathVariable Long id, @RequestBody TripDTO tripDTO) {
        Optional<TripDTO> updatedTrip = tripService.updateTrip(id, tripDTO);
        if (updatedTrip == null) {
            return ResponseEntity.badRequest().body("El barco no existe");
        }
        return updatedTrip.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar viaje
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long id) {
        tripService.deleteTrip(id);
        return ResponseEntity.noContent().build();
    }
}
