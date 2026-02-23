package com.example.GSVessel.Mapper;

import com.example.GSVessel.DTO.MaintenanceDTO;
import com.example.GSVessel.Model.Maintenance;

public class MaintenanceMapper {

    public static MaintenanceDTO toDTO(Maintenance maintenance) {
        if (maintenance == null) return null;

        Long equipmentId = null;
        String equipmentName = null;
        Long shipId = null;
        String shipName = null;

        if (maintenance.getEquipment() != null) {
            equipmentId = maintenance.getEquipment().getId();
            equipmentName = maintenance.getEquipment().getName();

            if (maintenance.getEquipment().getShip() != null) {
                shipId = maintenance.getEquipment().getShip().getId();
                shipName = maintenance.getEquipment().getShip().getName();
            }
        }

        return MaintenanceDTO.builder()
                .id(maintenance.getId())
                .fecha(maintenance.getFecha())
                .costo(maintenance.getCosto())
                .descripcion(maintenance.getDescripcion())
                .taller(maintenance.getTaller())
                .tipoMaintenance(maintenance.getTipoMaintenance())
                .imageUrl(maintenance.getImageUrl())
                .equipmentId(equipmentId)
                .equipmentName(equipmentName)
                .shipId(shipId)
                .shipName(shipName)
                .build();
    }
}
