package com.example.GSVessel.Service;

import com.example.GSVessel.DTO.EquipmentDTO;
import com.example.GSVessel.Model.Equipment;
import com.example.GSVessel.Model.Ship;
import com.example.GSVessel.Model.Enums.EquipmentCategory;
import com.example.GSVessel.Repository.EquipmentRepository;
import com.example.GSVessel.Repository.ShipRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
        return equipments.stream()
                .map(e -> convertToDTO(e))
                .collect(Collectors.toList());
    }

    // Guardar nuevo equipo
    public Equipment saveEquipment(Equipment equipment) {
        return equipmentRepository.save(equipment);
    }

    // Obtener equipo por ID
    public Optional<Equipment> getEquipmentById(Long id) {
        return equipmentRepository.findById(id);
    }

    // Actualizar equipo
    public Equipment updateEquipment(Equipment equipment) {
        return equipmentRepository.save(equipment);
    }

    // Actualizar a partir de DTO
    public EquipmentDTO updateEquipment(Long id, EquipmentDTO equipmentDTO) {
        Optional<Equipment> optional = equipmentRepository.findById(id);
        if (!optional.isPresent()) return null;

        Equipment equipment = optional.get();
        equipment.setName(equipmentDTO.getName());
        equipment.setCategory(equipmentDTO.getCategory());
        equipment.setLocation(equipmentDTO.getLocation());
        equipment.setConsumption(equipmentDTO.getConsumption());
        equipment.setHoursUsed(equipmentDTO.getHoursUsed());
        equipment.setBudget(equipmentDTO.getBudget());
        equipment.setDescription(equipmentDTO.getDescription());
        equipment.setImageUrl(equipmentDTO.getImageUrl());

        if (equipmentDTO.getShipId() != null) {
            Optional<Ship> ship = shipRepository.findById(equipmentDTO.getShipId());
            ship.ifPresent(equipment::setShip);
        }

        Equipment updated = equipmentRepository.save(equipment);
        return convertToDTO(updated);
    }

    // Eliminar equipo
    public boolean deleteEquipment(Long id) {
        Optional<Equipment> optional = equipmentRepository.findById(id);
        if (optional.isPresent()) {
            equipmentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Filtrar por barco
    public List<EquipmentDTO> getEquipmentByShip(Long shipId) {
        List<Equipment> equipments = equipmentRepository.findByShipId(shipId);
        return equipments.stream().map(e -> convertToDTO(e)).collect(Collectors.toList());
    }

    // Filtrar por categoría
    public List<EquipmentDTO> getEquipmentByCategory(EquipmentCategory category) {
        List<Equipment> equipments = equipmentRepository.findByCategory(category);
        return equipments.stream().map(e -> convertToDTO(e)).collect(Collectors.toList());
    }
}
