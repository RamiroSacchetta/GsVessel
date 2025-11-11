package com.example.GSVessel.Service;

import com.example.GSVessel.DTO.CrewMemberDTO;
import com.example.GSVessel.Exception.EntityNotFoundException;
import com.example.GSVessel.Exception.ListNoContentException;
import com.example.GSVessel.Model.CrewMember;
import com.example.GSVessel.Model.Trip;
import com.example.GSVessel.Repository.CrewMemberRepository;
import com.example.GSVessel.Repository.TripRepository;
import com.example.GSVessel.Mapper.CrewMemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CrewMemberService {

    @Autowired
    private CrewMemberRepository crewMemberRepository;

    @Autowired
    private TripRepository tripRepository;

    // Crear tripulante
    public CrewMemberDTO create(CrewMemberDTO dto) {
        Trip trip = tripRepository.findById(dto.getTripId())
                .orElseThrow(() -> new EntityNotFoundException("Viaje no encontrado con id: " + dto.getTripId()));
        CrewMember crew = CrewMemberMapper.toEntity(dto, trip);
        CrewMember saved = crewMemberRepository.save(crew);
        return CrewMemberMapper.toDTO(saved);
    }

    // Listar todos los tripulantes
    public List<CrewMemberDTO> findAll() {
        List<CrewMember> crewList = crewMemberRepository.findAll();
        if (crewList.isEmpty()) {
            throw new ListNoContentException("No se encontraron tripulantes");
        }
        return crewList.stream()
                .map(CrewMemberMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar tripulante por id
    public CrewMemberDTO findById(Long id) {
        CrewMember crew = crewMemberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tripulante no encontrado con id: " + id));
        return CrewMemberMapper.toDTO(crew);
    }

    // Buscar tripulantes por viaje (nuevo)
    public List<CrewMemberDTO> findByTripId(Long tripId) {
        List<CrewMember> crewList = crewMemberRepository.findByTripId(tripId);
        if (crewList.isEmpty()) {
            throw new ListNoContentException("No se encontraron tripulantes para el viaje con id: " + tripId);
        }
        return crewList.stream()
                .map(CrewMemberMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Actualizar tripulante
    public CrewMemberDTO update(Long id, CrewMemberDTO dto) {
        CrewMember existing = crewMemberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tripulante no encontrado con id: " + id));

        if (dto.getName() != null) existing.setName(dto.getName());
        if (dto.getRole() != null) existing.setRole(dto.getRole());
        if (dto.getContact() != null) existing.setContact(dto.getContact());
        if (dto.getTripId() != null) {
            Trip trip = tripRepository.findById(dto.getTripId())
                    .orElseThrow(() -> new EntityNotFoundException("Viaje no encontrado con id: " + dto.getTripId()));
            existing.setTrip(trip);
        }

        CrewMember updated = crewMemberRepository.save(existing);
        return CrewMemberMapper.toDTO(updated);
    }

    // Eliminar tripulante
    public void delete(Long id) {
        if (!crewMemberRepository.existsById(id)) {
            throw new EntityNotFoundException("Tripulante no encontrado con id: " + id);
        }
        crewMemberRepository.deleteById(id);
    }
}