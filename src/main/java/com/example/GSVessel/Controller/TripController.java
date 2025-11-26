package com.example.GSVessel.Controller;

import com.example.GSVessel.Model.Trip;
import com.example.GSVessel.Service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @GetMapping
    public List<Trip> getAll() {
        return tripService.findAll();
    }

   /* @GetMapping("/{id}")
    public Trip getById(@PathVariable Long id) {
        return tripService.findById(id);
    }

    */

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Trip trip) {
        Map<String, Object> response = tripService.createTrip(trip);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tripService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/returnDate")
    public ResponseEntity<Trip> updateReturnDate(@PathVariable Long id,
                                                 @RequestBody Map<String, String> body) {
        LocalDate returnDate = LocalDate.parse(body.get("returnDate"));
        Trip updatedTrip = tripService.updateReturnDate(id, returnDate);
        return ResponseEntity.ok(updatedTrip);
    }

    @GetMapping("/{shipId}")
    public ResponseEntity<List<Trip>> getByShipId(@PathVariable Long shipId) {
        List<Trip> trips = tripService.findByShipId(shipId);
        if (trips == null || trips.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(trips);
    }
}