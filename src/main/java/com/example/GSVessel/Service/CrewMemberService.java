package com.example.GSVessel.Service;

import com.example.GSVessel.DTO.CrewMemberDTO;
import com.example.GSVessel.Model.CrewMember;
import com.example.GSVessel.Model.Ship;
import com.example.GSVessel.Repository.CrewMemberRepository;
import com.example.GSVessel.Repository.ShipRepository;
import com.example.GSVessel.Repository.Mapper.CrewMemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CrewMemberService {

    @Autowired
    private CrewMemberRepository crewMemberRepository;

    @Autowired
    private ShipRepository shipRepository;

    public CrewMemberDTO create(CrewMemberDTO dto) {
        Ship ship = shipRepository.findById(dto.getShipId())
                .orElseThrow(() -> new RuntimeException("Barco no encontrado con id: " + dto.getShipId()));
        CrewMember crew = CrewMemberMapper.toEntity(dto, ship);
        CrewMember saved = crewMemberRepository.save(crew);
        return CrewMemberMapper.toDTO(saved);
    }

    public List<CrewMemberDTO> findAll() {
        return crewMemberRepository.findAll()
                .stream()
                .map(CrewMemberMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CrewMemberDTO findById(Long id) {
        CrewMember crew = crewMemberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tripulante no encontrado con id: " + id));
        return CrewMemberMapper.toDTO(crew);
    }

    public List<CrewMemberDTO> findByShipId(Long shipId) {
        return crewMemberRepository.findByShipId(shipId)
                .stream()
                .map(CrewMemberMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CrewMemberDTO update(Long id, CrewMemberDTO dto) {
        CrewMember existing = crewMemberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tripulante no encontrado con id: " + id));

        if (dto.getName() != null) existing.setName(dto.getName());
        if (dto.getRole() != null) existing.setRole(dto.getRole());
        if (dto.getContact() != null) existing.setContact(dto.getContact());
        if (dto.getShipId() != null) {
            Ship ship = shipRepository.findById(dto.getShipId())
                    .orElseThrow(() -> new RuntimeException("Barco no encontrado con id: " + dto.getShipId()));
            existing.setShip(ship);
        }

        CrewMember updated = crewMemberRepository.save(existing);
        return CrewMemberMapper.toDTO(updated);
    }

    public void delete(Long id) {
        if (!crewMemberRepository.existsById(id)) {
            throw new RuntimeException("Tripulante no encontrado con id: " + id);
        }
        crewMemberRepository.deleteById(id);
    }
}
