package com.example.GSVessel.Service;

import com.example.GSVessel.DTO.MaintenanceDTO;
import com.example.GSVessel.Exception.BusinessException;
import com.example.GSVessel.Exception.EntityNotFoundException;
import com.example.GSVessel.Exception.ListNoContentException;
import com.example.GSVessel.Model.Enums.TipoMaintenance;
import com.example.GSVessel.Model.Maintenance;
import com.example.GSVessel.Repository.MaintenanceRepository;
import com.example.GSVessel.Repository.Mappers.MaintenanceMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;

    public MaintenanceService(MaintenanceRepository maintenanceRepository) {
        this.maintenanceRepository = maintenanceRepository;
    }

    // Crear mantenimiento
    public MaintenanceDTO create(MaintenanceDTO dto) {
        if (dto.getFecha() == null) {
            throw new BusinessException("La fecha es obligatoria");
        }
        if (dto.getCosto() == null) {
            throw new BusinessException("El costo es obligatorio");
        }
        if (dto.getTipoMaintenance() == null) {
            throw new BusinessException("El tipo de mantenimiento es obligatorio");
        }

        Maintenance maintenance = MaintenanceMapper.toEntity(dto);
        Maintenance saved = maintenanceRepository.save(maintenance);
        return MaintenanceMapper.toDTO(saved);
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

    // Actualizar mantenimiento
    public MaintenanceDTO update(Long id, MaintenanceDTO dto) {
        Maintenance existing = maintenanceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mantenimiento no encontrado con id: " + id));

        if (dto.getFecha() != null) existing.setFecha(dto.getFecha());
        if (dto.getCosto() != null) existing.setCosto(dto.getCosto());
        if (dto.getDescripcion() != null) existing.setDescripcion(dto.getDescripcion());
        if (dto.getTipoMaintenance() != null) existing.setTipoMaintenance(dto.getTipoMaintenance());

        Maintenance updated = maintenanceRepository.save(existing);
        return MaintenanceMapper.toDTO(updated);
    }

    // Eliminar mantenimiento
    public void delete(Long id) {
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mantenimiento no encontrado con id: " + id));
        maintenanceRepository.delete(maintenance);
    }
}

