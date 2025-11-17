package com.example.GSVessel.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.GSVessel.DTO.MaintenanceDTO;
import com.example.GSVessel.Exception.BusinessException;
import com.example.GSVessel.Exception.EntityNotFoundException;
import com.example.GSVessel.Exception.ListNoContentException;
import com.example.GSVessel.Model.Enums.TipoMaintenance;
import com.example.GSVessel.Model.Maintenance;
import com.example.GSVessel.Model.Equipment;
import com.example.GSVessel.Repository.MaintenanceRepository;
import com.example.GSVessel.Repository.EquipmentRepository;
import com.example.GSVessel.Mapper.MaintenanceMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final EquipmentRepository equipmentRepository;
    private final Cloudinary cloudinary;

    public MaintenanceService(MaintenanceRepository maintenanceRepository, EquipmentRepository equipmentRepository, Cloudinary cloudinary) {
        this.maintenanceRepository = maintenanceRepository;
        this.equipmentRepository = equipmentRepository;
        this.cloudinary = cloudinary;
    }

    // Subir imagen a Cloudinary (opcional)
    private String uploadImage(MultipartFile image) {
        if (image == null || image.isEmpty()) return null;
        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
            return (String) uploadResult.get("url");
        } catch (IOException e) {
            throw new RuntimeException("Error al subir imagen a Cloudinary: " + e.getMessage(), e);
        }
    }

    // Crear mantenimiento
    public MaintenanceDTO create(MaintenanceDTO dto) {
        validateForCreate(dto);

        Equipment equipment = equipmentRepository.findById(dto.getEquipmentId())
                .orElseThrow(() -> new EntityNotFoundException("Equipo no encontrado con id: " + dto.getEquipmentId()));

        Maintenance maintenance = new Maintenance();
        maintenance.setFecha(dto.getFecha());
        maintenance.setCosto(dto.getCosto()); // ahora opcional
        maintenance.setDescripcion(dto.getDescripcion());
        maintenance.setTipoMaintenance(dto.getTipoMaintenance());
        maintenance.setEquipment(equipment);
        maintenance.setTaller(dto.getTaller());

        String imageUrl = uploadImage(dto.getImage());
        maintenance.setImageUrl(imageUrl);

        Maintenance saved = maintenanceRepository.save(maintenance);
        return toDTOWithEquipmentName(saved);
    }

    // Actualizar mantenimiento
    public MaintenanceDTO update(Long id, MaintenanceDTO dto) {
        Maintenance existing = maintenanceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mantenimiento no encontrado con id: " + id));

        if (dto.getFecha() != null) existing.setFecha(dto.getFecha());
        if (dto.getCosto() != null) existing.setCosto(dto.getCosto()); // opcional
        if (dto.getDescripcion() != null) existing.setDescripcion(dto.getDescripcion());
        if (dto.getTipoMaintenance() != null) existing.setTipoMaintenance(dto.getTipoMaintenance());

        if (dto.getEquipmentId() != null) {
            Equipment equipment = equipmentRepository.findById(dto.getEquipmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Equipo no encontrado con id: " + dto.getEquipmentId()));
            existing.setEquipment(equipment);
        }

        if (dto.getTaller() != null) existing.setTaller(dto.getTaller());

        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            existing.setImageUrl(uploadImage(dto.getImage()));
        }

        Maintenance updated = maintenanceRepository.save(existing);
        return toDTOWithEquipmentName(updated);
    }

    private void validateForCreate(MaintenanceDTO dto) {
        if (dto.getFecha() == null) throw new BusinessException("La fecha es obligatoria");
        if (dto.getTipoMaintenance() == null) throw new BusinessException("El tipo de mantenimiento es obligatorio");
        if (dto.getEquipmentId() == null) throw new BusinessException("El ID del equipo es obligatorio");
    }

    public List<MaintenanceDTO> findAll() {
        List<Maintenance> list = maintenanceRepository.findAll();
        if (list.isEmpty()) throw new ListNoContentException("No se encontraron mantenimientos");
        return list.stream().map(this::toDTOWithEquipmentName).collect(Collectors.toList());
    }

    public MaintenanceDTO findById(Long id) {
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mantenimiento no encontrado con id: " + id));
        return toDTOWithEquipmentName(maintenance);
    }

    public List<MaintenanceDTO> findByTipo(TipoMaintenance tipo) {
        List<Maintenance> list = maintenanceRepository.findByTipoMaintenance(tipo);
        if (list.isEmpty()) throw new ListNoContentException("No hay mantenimientos de tipo: " + tipo);
        return list.stream().map(this::toDTOWithEquipmentName).collect(Collectors.toList());
    }

    public void delete(Long id) {
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mantenimiento no encontrado con id: " + id));
        maintenanceRepository.delete(maintenance);
    }

    private MaintenanceDTO toDTOWithEquipmentName(Maintenance maintenance) {
        MaintenanceDTO dto = MaintenanceMapper.toDTO(maintenance);
        if (maintenance.getEquipment() != null) {
            dto.setEquipmentName(maintenance.getEquipment().getName());
        }
        return dto;
    }
}
