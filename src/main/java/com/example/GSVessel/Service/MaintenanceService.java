package com.example.GSVessel.Service;

import com.example.GSVessel.DTO.MaintenanceDTO;
import com.example.GSVessel.Model.Enums.TipoMaintenance;
import com.example.GSVessel.Model.Maintenance;
import com.example.GSVessel.Repository.MaintenanceRepository;
import com.example.GSVessel.Repository.Mappers.MaintenanceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaintenanceService {

    @Autowired
    private MaintenanceRepository maintenanceRepository;



    public MaintenanceDTO create(MaintenanceDTO dto) {
        if (dto.getFecha() == null) {
            throw new IllegalArgumentException("La fecha es obligatoria");
        }
        if (dto.getCosto() == null) {
            throw new IllegalArgumentException("El costo es obligatorio");
        }
        if (dto.getTipoMaintenance() == null) {
            throw new IllegalArgumentException("El tipo de mantenimiento es obligatorio");
        }

        Maintenance maintenance = MaintenanceMapper.toEntity(dto);
        Maintenance saved = maintenanceRepository.save(maintenance);
        return MaintenanceMapper.toDTO(saved);
    }

    public List<MaintenanceDTO> findAll() {
        return maintenanceRepository.findAll()
                .stream()
                .map(MaintenanceMapper::toDTO)
                .collect(Collectors.toList());
    }

    public MaintenanceDTO findById(Long id) {
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mantenimiento no encontrado con id: " + id));
        return MaintenanceMapper.toDTO(maintenance);
    }

    public List<MaintenanceDTO> findByTipo(TipoMaintenance tipo) {
        List<Maintenance> mantenimientos = maintenanceRepository.findByTipoMaintenance(tipo);
        if (mantenimientos.isEmpty()) {
            throw new RuntimeException("No hay mantenimientos de tipo: " + tipo);
        }
        return mantenimientos.stream()
                .map(MaintenanceMapper::toDTO)
                .collect(Collectors.toList());
    }

    public MaintenanceDTO update(Long id, MaintenanceDTO dto) {
        Maintenance existing = maintenanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mantenimiento no encontrado con id: " + id));

        if (dto.getFecha() != null) {
            existing.setFecha(dto.getFecha());
        }
        if (dto.getCosto() != null) {
            existing.setCosto(dto.getCosto());
        }
        if (dto.getDescripcion() != null) {
            existing.setDescripcion(dto.getDescripcion());
        }
        if (dto.getTipoMaintenance() != null) {
            existing.setTipoMaintenance(dto.getTipoMaintenance());
        }

        Maintenance updated = maintenanceRepository.save(existing);
        return MaintenanceMapper.toDTO(updated);
    }

    public void delete(Long id) {
        if (!maintenanceRepository.existsById(id)) {
            throw new RuntimeException("Mantenimiento no encontrado con id: " + id);
        }
        maintenanceRepository.deleteById(id);
    }
}
