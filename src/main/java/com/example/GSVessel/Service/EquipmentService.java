package com.example.GSVessel.Service;

import com.example.GSVessel.DTO.EquipmentDTO;
import com.example.GSVessel.Exception.EntityNotFoundException;
import com.example.GSVessel.Exception.ListNoContentException;
import com.example.GSVessel.Model.Equipment;
import com.example.GSVessel.Model.Ship;
import com.example.GSVessel.Model.Enums.EquipmentCategory;
import com.example.GSVessel.Repository.EquipmentRepository;
import com.example.GSVessel.Repository.ShipRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final ShipRepository shipRepository;

    public EquipmentService(EquipmentRepository equipmentRepository, ShipRepository shipRepository) {
        this.equipmentRepository = equipmentRepository;
        this.shipRepository = shipRepository;
    }

    // Convertir a DTO
    public EquipmentDTO convertToDTO(Equipment equipment) {
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

    // Listar todos los equipos
    public List<EquipmentDTO> getAllEquipment() {
        List<Equipment> equipments = equipmentRepository.findAll();
        if (equipments.isEmpty()) {
            throw new ListNoContentException("No se encontraron equipos");
        }
        return equipments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Guardar nuevo equipo
    public Equipment saveEquipment(Equipment equipment) {
        return equipmentRepository.save(equipment);
    }

    // Obtener equipo por ID
    public Equipment getEquipmentById(Long id) {
        return equipmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipo no encontrado con id: " + id));
    }

    // Actualizar equipo
    public Equipment updateEquipment(Equipment equipment) {
        if (!equipmentRepository.existsById(equipment.getId())) {
            throw new EntityNotFoundException("Equipo no encontrado con id: " + equipment.getId());
        }
        return equipmentRepository.save(equipment);
    }

    // Actualizar a partir de DTO
    public EquipmentDTO updateEquipment(Long id, EquipmentDTO equipmentDTO) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipo no encontrado con id: " + id));

        equipment.setName(equipmentDTO.getName());
        equipment.setCategory(equipmentDTO.getCategory());
        equipment.setLocation(equipmentDTO.getLocation());
        equipment.setConsumption(equipmentDTO.getConsumption());
        equipment.setHoursUsed(equipmentDTO.getHoursUsed());
        equipment.setBudget(equipmentDTO.getBudget());
        equipment.setDescription(equipmentDTO.getDescription());
        equipment.setImageUrl(equipmentDTO.getImageUrl());

        if (equipmentDTO.getShipId() != null) {
            Ship ship = shipRepository.findById(equipmentDTO.getShipId())
                    .orElseThrow(() -> new EntityNotFoundException("Barco no encontrado con id: " + equipmentDTO.getShipId()));
            equipment.setShip(ship);
        }

        Equipment updated = equipmentRepository.save(equipment);
        return convertToDTO(updated);
    }

    public void deleteEquipment(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipo no encontrado con id: " + id));
        equipmentRepository.delete(equipment);
    }

    // Filtrar por barco
    public List<EquipmentDTO> getEquipmentByShip(Long shipId) {
        List<Equipment> equipments = equipmentRepository.findByShipId(shipId);
        if (equipments.isEmpty()) {
            throw new ListNoContentException("No se encontraron equipos para el barco con id: " + shipId);
        }
        return equipments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Filtrar por categoría
    public List<EquipmentDTO> getEquipmentByCategory(EquipmentCategory category) {
        List<Equipment> equipments = equipmentRepository.findByCategory(category);
        if (equipments.isEmpty()) {
            throw new ListNoContentException("No se encontraron equipos para la categoría: " + category);
        }
        return equipments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
}
