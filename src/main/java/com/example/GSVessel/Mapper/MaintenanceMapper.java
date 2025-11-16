package com.example.GSVessel.Mapper;

import com.example.GSVessel.DTO.MaintenanceDTO;
import com.example.GSVessel.Model.Maintenance;

public class MaintenanceMapper {

    public static MaintenanceDTO toDTO(Maintenance maintenance) {
        if (maintenance == null) return null;

        // Manejo seguro de equipment
        String equipmentName = null;
        if (maintenance.getEquipment() != null) {
            equipmentName = maintenance.getEquipment().getName();
        }

        return MaintenanceDTO.builder()
                .id(maintenance.getId())
                .fecha(maintenance.getFecha())
                .costo(maintenance.getCosto())
                .descripcion(maintenance.getDescripcion())
                .taller(maintenance.getTaller())
                .tipoMaintenance(maintenance.getTipoMaintenance())
                .imageUrl(maintenance.getImageUrl())
                .equipmentName(equipmentName) // Asignamos el nombre o null si no hay equipo
                .build();
    }
}