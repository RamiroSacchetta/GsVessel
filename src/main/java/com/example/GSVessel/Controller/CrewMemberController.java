package com.example.GSVessel.Controller;

import com.example.GSVessel.DTO.CrewMemberDTO;
import com.example.GSVessel.Service.CrewMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crew")
public class CrewMemberController {

    @Autowired
    private CrewMemberService crewMemberService;

    @PostMapping
    public ResponseEntity<CrewMemberDTO> create(@RequestBody CrewMemberDTO dto) {
        return ResponseEntity.status(201).body(crewMemberService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<CrewMemberDTO>> getAll() {
        return ResponseEntity.ok(crewMemberService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CrewMemberDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(crewMemberService.findById(id));
    }

    @GetMapping("/trip/{tripId}")
    public ResponseEntity<List<CrewMemberDTO>> getByTrip(@PathVariable Long tripId) {
        return ResponseEntity.ok(crewMemberService.findByTripId(tripId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CrewMemberDTO> update(@PathVariable Long id, @RequestBody CrewMemberDTO dto) {
        return ResponseEntity.ok(crewMemberService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        crewMemberService.delete(id);
        return ResponseEntity.noContent().build();
    }
}