package com.example.GSVessel.Service.;

import com.example.GSVessel.DTO.EquipmentDTO;
import com.example.GSVessel.Model.Equipment;
import com.example.GSVessel.Model.Ship;
import com.example.GSVessel.Repository.EquipmentRepository;
import com.example.GSVessel.Repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private ShipRepository shipRepository;

    // Crear un nuevo equipment
    public EquipmentDTO createEquipment(EquipmentDTO dto) {
        Equipment equipment = mapDtoToEntity(dto);

        if (dto.getShipId() != null) {
            Ship ship = shipRepository.findById(dto.getShipId())
                    .orElseThrow(() -> new RuntimeException("Ship no encontrado con ID: " + dto.getShipId()));
            equipment.setShip(ship);
        }

        Equipment saved = equipmentRepository.save(equipment);
        return mapEntityToDto(saved);
    }

    // Listar todos los equipment
    public List<EquipmentDTO> getAllEquipment() {
        return equipmentRepository.findAll()
                .stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    // Obtener equipment por ID
    public EquipmentDTO getEquipmentById(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment no encontrado con ID: " + id));
        return mapEntityToDto(equipment);
    }

    // Actualizar equipment
    public EquipmentDTO updateEquipment(Long id, EquipmentDTO dto) {
        Equipment existing = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment no encontrado con ID: " + id));

        existing.setName(dto.getName());
        existing.setCategory(dto.getCategory());
        existing.setLocation(dto.getLocation());
        existing.setConsumption(dto.getConsumption());
        existing.setHoursUsed(dto.getHoursUsed());
        existing.setBudget(dto.getBudget());
        existing.setDescription(dto.getDescription());
        existing.setImageUrl(dto.getImageUrl());

        if (dto.getShipId() != null) {
            Ship ship = shipRepository.findById(dto.getShipId())
                    .orElseThrow(() -> new RuntimeException("Ship no encontrado con ID: " + dto.getShipId()));
            existing.setShip(ship);
        } else {
            existing.setShip(null);
        }

        Equipment updated = equipmentRepository.save(existing);
        return mapEntityToDto(updated);
    }

    // Eliminar equipment
    public void deleteEquipment(Long id) {
        Equipment existing = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment no encontrado con ID: " + id));
        equipmentRepository.delete(existing);
    }

    // --- Métodos privados de mapeo DTO <-> Entity ---
    private Equipment mapDtoToEntity(EquipmentDTO dto) {
        Equipment equipment = new Equipment();
        equipment.setName(dto.getName());
        equipment.setCategory(dto.getCategory());
        equipment.setLocation(dto.getLocation());
        equipment.setConsumption(dto.getConsumption());
        equipment.setHoursUsed(dto.getHoursUsed());
        equipment.setBudget(dto.getBudget());
        equipment.setDescription(dto.getDescription());
        equipment.setImageUrl(dto.getImageUrl());
        return equipment;
    }

    private EquipmentDTO mapEntityToDto(Equipment equipment) {
        Long shipId = equipment.getShip() != null ? equipment.getShip().getId() : null;
        return new EquipmentDTO(
                equipment.getId(),
                equipment.getName(),
                equipment.getCategory(),
                equipment.getLocation(),
                equipment.getConsumption(),
                equipment.getHoursUsed(),
                equipment.getBudget(),
                equipment.getDescription(),
                equipment.getImageUrl(),
                shipId
        );
    }
}
