package com.example.GSVessel.Repository;

import com.example.GSVessel.Model.MantenimientoPreventivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MantenimientoPreventivoRepository extends JpaRepository<MantenimientoPreventivo, Long> {

    // Buscar por el ID del equipo (parte)
    List<MantenimientoPreventivo> findByEquipmentId(Long equipmentId);

    // Buscar por el ID del barco (a través de la relación de Equipment)
    @Query("SELECT mp FROM MantenimientoPreventivo mp WHERE mp.equipment.ship.id = :shipId")
    List<MantenimientoPreventivo> findByShipId(@Param("shipId") Long shipId);

    // Buscar por ID del barco y categoría del equipo
    @Query("SELECT mp FROM MantenimientoPreventivo mp WHERE mp.equipment.ship.id = :shipId AND mp.equipment.category = :category")
    List<MantenimientoPreventivo> findByShipIdAndEquipmentCategory(@Param("shipId") Long shipId, @Param("category") String category);

    // Opcional: Buscar todos los mantenimientos preventivos con alerta activa
    @Query("SELECT mp FROM MantenimientoPreventivo mp WHERE (mp.equipment.hoursUsed >= (mp.horasCambio - mp.horasAviso))")
    List<MantenimientoPreventivo> findWithActiveAlerts();
}