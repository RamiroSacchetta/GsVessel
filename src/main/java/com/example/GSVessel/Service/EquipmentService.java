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

    // Método auxiliar para subir imagen a Cloudinary
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

    // Convertir entidad a DTO (sin imagen, solo URL)
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
                shipId,
                null // No incluimos MultipartFile en el DTO de salida
        );
    }

    // Guardar nuevo equipo (usando el image del DTO)
    public EquipmentDTO saveEquipment(EquipmentDTO equipmentDTO) {
        Equipment equipment = new Equipment();
        equipment.setName(equipmentDTO.getName());
        equipment.setCategory(equipmentDTO.getCategory());
        equipment.setLocation(equipmentDTO.getLocation());
        equipment.setConsumption(equipmentDTO.getConsumption());
        equipment.setHoursUsed(equipmentDTO.getHoursUsed());
        equipment.setBudget(equipmentDTO.getBudget());
        equipment.setDescription(equipmentDTO.getDescription());

        // Subir imagen si está presente en el DTO
        MultipartFile image = equipmentDTO.getImage();
        if (image != null && !image.isEmpty()) {
            String imageUrl = uploadImage(image);
            equipment.setImageUrl(imageUrl);
        }

        // Asociar barco si se proporciona
        if (equipmentDTO.getShipId() != null) {
            Ship ship = shipRepository.findById(equipmentDTO.getShipId())
                    .orElseThrow(() -> new EntityNotFoundException("Barco no encontrado con id: " + equipmentDTO.getShipId()));
            equipment.setShip(ship);
        }

        Equipment saved = equipmentRepository.save(equipment);
        return convertToDTO(saved);
    }

    // Actualizar equipo (con posible nueva imagen)
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

        // Si se envía una nueva imagen, reemplazar la URL
        MultipartFile newImage = equipmentDTO.getImage();
        if (newImage != null && !newImage.isEmpty()) {
            String newImageUrl = uploadImage(newImage);
            equipment.setImageUrl(newImageUrl);
        }
        // Si no hay imagen nueva, mantenemos la URL actual (no la borramos)

        // Actualizar barco si cambia
        if (equipmentDTO.getShipId() != null) {
            Ship ship = shipRepository.findById(equipmentDTO.getShipId())
                    .orElseThrow(() -> new EntityNotFoundException("Barco no encontrado con id: " + equipmentDTO.getShipId()));
            equipment.setShip(ship);
        } else {
            // Opcional: si shipId es null, ¿desvincular el equipo del barco?
            // equipment.setShip(null);
        }

        Equipment updated = equipmentRepository.save(equipment);
        return convertToDTO(updated);
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

    // Obtener equipo por ID
    public EquipmentDTO getEquipmentById(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipo no encontrado con id: " + id));
        return convertToDTO(equipment);
    }

    // Eliminar equipo
    public void deleteEquipment(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipo no encontrado con id: " + id));
        // Opcional: eliminar imagen de Cloudinary (requiere public_id, no lo tenemos)
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