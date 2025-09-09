package com.example.GSVessel.Repository.Mappers;

import com.example.GSVessel.DTO.MaintenanceDTO;
import com.example.GSVessel.Model.Maintenance;

public class MaintenanceMapper {

    public static MaintenanceDTO toDTO(Maintenance maintenance) {
        return MaintenanceDTO.builder()
                .id(maintenance.getId())
                .fecha(maintenance.getFecha())
                .costo(maintenance.getCosto())
                .Descripcion(maintenance.getDescripcion())
                .tipoMaintenance(maintenance.getTipoMaintenance())
                .build();
    }

    public static Maintenance toEntity(MaintenanceDTO dto) {
        return Maintenance.builder()
                .id(dto.getId())
                .fecha(dto.getFecha())
                .costo(dto.getCosto())
                .descripcion(dto.getDescripcion())
                .tipoMaintenance(dto.getTipoMaintenance())
                .build();
    }
}
