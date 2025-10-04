package com.example.GSVessel.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.GSVessel.DTO.MaintenanceDTO;
import com.example.GSVessel.Exception.BusinessException;
import com.example.GSVessel.Exception.EntityNotFoundException;
import com.example.GSVessel.Exception.ListNoContentException;
import com.example.GSVessel.Model.Enums.TipoMaintenance;
import com.example.GSVessel.Model.Maintenance;
import com.example.GSVessel.Repository.MaintenanceRepository;
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
    private final Cloudinary cloudinary;

    public MaintenanceService(MaintenanceRepository maintenanceRepository, Cloudinary cloudinary) {
        this.maintenanceRepository = maintenanceRepository;
        this.cloudinary = cloudinary;
    }

    // Método privado para subir imagen a Cloudinary
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

    // Crear mantenimiento
    public MaintenanceDTO create(MaintenanceDTO dto) {
        validateForCreate(dto);

        Maintenance maintenance = new Maintenance();
        maintenance.setFecha(dto.getFecha());
        maintenance.setCosto(dto.getCosto());
        maintenance.setDescripcion(dto.getDescripcion());
        maintenance.setTipoMaintenance(dto.getTipoMaintenance());

        // Subir imagen si existe y guardar la URL
        String imageUrl = uploadImage(dto.getImage());
        maintenance.setImageUrl(imageUrl);

        Maintenance saved = maintenanceRepository.save(maintenance);
        return MaintenanceMapper.toDTO(saved);
    }

    // Actualizar mantenimiento
    public MaintenanceDTO update(Long id, MaintenanceDTO dto) {
        Maintenance existing = maintenanceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mantenimiento no encontrado con id: " + id));

        // Actualizar campos simples
        if (dto.getFecha() != null) existing.setFecha(dto.getFecha());
        if (dto.getCosto() != null) existing.setCosto(dto.getCosto());
        if (dto.getDescripcion() != null) existing.setDescripcion(dto.getDescripcion());
        if (dto.getTipoMaintenance() != null) existing.setTipoMaintenance(dto.getTipoMaintenance());

        // Si se envía una nueva imagen, reemplazar la URL
        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            String newImageUrl = uploadImage(dto.getImage());
            existing.setImageUrl(newImageUrl);
        }
        // Si no hay imagen nueva, mantenemos la URL actual (no la borramos)

        Maintenance updated = maintenanceRepository.save(existing);
        return MaintenanceMapper.toDTO(updated);
    }

    // Validaciones para creación
    private void validateForCreate(MaintenanceDTO dto) {
        if (dto.getFecha() == null) {
            throw new BusinessException("La fecha es obligatoria");
        }
        if (dto.getCosto() == null) {
            throw new BusinessException("El costo es obligatorio");
        }
        if (dto.getTipoMaintenance() == null) {
            throw new BusinessException("El tipo de mantenimiento es obligatorio");
        }
    }

    // Listar todos los mantenimientos
    public List<MaintenanceDTO> findAll() {
        List<Maintenance> list = maintenanceRepository.findAll();
        if (list.isEmpty()) {
            throw new ListNoContentException("No se encontraron mantenimientos");
        }
        return list.stream()
                .map(MaintenanceMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar mantenimiento por id
    public MaintenanceDTO findById(Long id) {
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mantenimiento no encontrado con id: " + id));
        return MaintenanceMapper.toDTO(maintenance);
    }

    // Buscar mantenimientos por tipo
    public List<MaintenanceDTO> findByTipo(TipoMaintenance tipo) {
        List<Maintenance> list = maintenanceRepository.findByTipoMaintenance(tipo);
        if (list.isEmpty()) {
            throw new ListNoContentException("No hay mantenimientos de tipo: " + tipo);
        }
        return list.stream()
                .map(MaintenanceMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Eliminar mantenimiento
    public void delete(Long id) {
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mantenimiento no encontrado con id: " + id));
        // Opcional: eliminar imagen de Cloudinary (requiere public_id)
        maintenanceRepository.delete(maintenance);
    }
}