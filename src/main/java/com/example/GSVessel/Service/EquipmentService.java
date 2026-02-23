package com.example.GSVessel.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.GSVessel.DTO.EquipmentDTO;
import com.example.GSVessel.Exception.EntityNotFoundException;
import com.example.GSVessel.Exception.ListNoContentException;
import com.example.GSVessel.Model.Equipment;
import com.example.GSVessel.Model.Ship;
import com.example.GSVessel.Model.Enums.EquipmentCategory;
import com.example.GSVessel.Repository.EquipmentRepository;
import com.example.GSVessel.Repository.ShipRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final ShipRepository shipRepository;
    private final Cloudinary cloudinary;

    public EquipmentService(
            EquipmentRepository equipmentRepository,
            ShipRepository shipRepository,
            Cloudinary cloudinary) {
        this.equipmentRepository = equipmentRepository;
        this.shipRepository = shipRepository;
        this.cloudinary = cloudinary;
    }

    private String uploadImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            return null;
        }
        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
            return (String) uploadResult.get("url");
        } catch (IOException e) {
            throw new RuntimeException("Error al subir la imagen a Cloudinary: " + e.getMessage(), e);
        }
    }

    public EquipmentDTO convertToDTO(Equipment equipment) {
        Long shipId = equipment.getShip() != null ? equipment.getShip().getId() : null;
        Long parentId = equipment.getParent() != null ? equipment.getParent().getId() : null;
        EquipmentDTO dto = new EquipmentDTO(
                equipment.getId(),
                equipment.getName(),
                equipment.getCategory(),
                equipment.getLocation(),
                equipment.getConsumption(),
                equipment.getHoursUsed(),
                equipment.getBudget(),
                equipment.getDescription(),
                shipId,
                equipment.getImageUrl()
        );
        dto.setParentId(parentId);
        return dto;
    }

    public EquipmentDTO saveEquipment(EquipmentDTO dto) {
        Equipment equipment = new Equipment();
        equipment.setName(dto.getName());
        equipment.setCategory(dto.getCategory());
        equipment.setLocation(dto.getLocation());
        equipment.setConsumption(dto.getConsumption());
        equipment.setHoursUsed(dto.getHoursUsed());
        equipment.setBudget(dto.getBudget());
        equipment.setDescription(dto.getDescription());

        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            equipment.setImageUrl(uploadImage(dto.getImage()));
        }

        if (dto.getShipId() != null) {
            Ship ship = shipRepository.findById(dto.getShipId())
                    .orElseThrow(() -> new EntityNotFoundException("Barco no encontrado con id: " + dto.getShipId()));
            equipment.setShip(ship);
        }

        // Asignar padre y sincronizar horas si aplica
        if (dto.getParentId() != null) {
            Equipment parent = equipmentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException("Equipo padre no encontrado con id: " + dto.getParentId()));
            equipment.setParent(parent);
            // Sincronizar horas del nuevo componente con el padre
            equipment.setHoursUsed(parent.getHoursUsed());
        }

        Equipment saved = equipmentRepository.save(equipment);
        return convertToDTO(saved);
    }

    public EquipmentDTO updateEquipment(Long id, EquipmentDTO dto) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipo no encontrado con id: " + id));

        equipment.setName(dto.getName());
        equipment.setCategory(dto.getCategory());
        equipment.setLocation(dto.getLocation());
        equipment.setConsumption(dto.getConsumption());
        equipment.setHoursUsed(dto.getHoursUsed());
        equipment.setBudget(dto.getBudget());
        equipment.setDescription(dto.getDescription());

        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            equipment.setImageUrl(uploadImage(dto.getImage()));
        }

        if (dto.getShipId() != null) {
            Ship ship = shipRepository.findById(dto.getShipId())
                    .orElseThrow(() -> new EntityNotFoundException("Barco no encontrado con id: " + dto.getShipId()));
            equipment.setShip(ship);
        }

        // Actualizar padre (o desvincular)
        if (dto.getParentId() != null) {
            Equipment parent = equipmentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException("Equipo padre no encontrado con id: " + dto.getParentId()));
            equipment.setParent(parent);
        } else {
            equipment.setParent(null);
        }

        Equipment updated = equipmentRepository.save(equipment);
        return convertToDTO(updated);
    }

    public EquipmentDTO getEquipmentById(Long id) {
        Equipment equipment = equipmentRepository.findWithHierarchyById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipo no encontrado con id: " + id));
        return convertToDTO(equipment);
    }

    public List<EquipmentDTO> getAllEquipment() {
        List<Equipment> list = equipmentRepository.findAll();
        if (list.isEmpty()) throw new ListNoContentException("No se encontraron equipos");
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<EquipmentDTO> getEquipmentByShip(Long shipId) {
        List<Equipment> list = equipmentRepository.findByShipId(shipId);
        if (list.isEmpty()) throw new ListNoContentException("No hay equipos para el barco con id: " + shipId);
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<EquipmentDTO> getEquipmentTreeByShip(Long shipId) {
        List<Equipment> roots = equipmentRepository.findByShipIdAndParentIsNull(shipId);
        if (roots.isEmpty()) throw new ListNoContentException("No hay equipos raíz para el barco con id: " + shipId);
        return roots.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    //  Filtrar por categoría
    public List<EquipmentDTO> getEquipmentByCategory(EquipmentCategory category) {
        List<Equipment> equipments = equipmentRepository.findByCategory(category);
        if (equipments.isEmpty()) {
            throw new ListNoContentException("No se encontraron equipos para la categoría: " + category);
        }
        return equipments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void deleteEquipment(Long id) {
        if (!equipmentRepository.existsById(id)) {
            throw new EntityNotFoundException("Equipo no encontrado con id: " + id);
        }
        equipmentRepository.deleteById(id);
    }

    // Reiniciar horas de un equipo específico (ej. tras reemplazo)
    public EquipmentDTO resetHours(Long equipmentId) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new EntityNotFoundException("Equipo no encontrado con id: " + equipmentId));
        equipment.setHoursUsed(0);
        Equipment saved = equipmentRepository.save(equipment);
        return convertToDTO(saved);
    }
}