package com.example.GSVessel.Mapper;

import com.example.GSVessel.DTO.MaintenanceDTO;
import com.example.GSVessel.Model.Maintenance;

public class MaintenanceMapper {

    public static MaintenanceDTO toDTO(Maintenance maintenance) {
        if (maintenance == null) return null;

        return MaintenanceDTO.builder()
                .id(maintenance.getId())
                .fecha(maintenance.getFecha())
                .costo(maintenance.getCosto())
                .Descripcion(maintenance.getDescripcion())
                .tipoMaintenance(maintenance.getTipoMaintenance())
                .build();
    }
}
