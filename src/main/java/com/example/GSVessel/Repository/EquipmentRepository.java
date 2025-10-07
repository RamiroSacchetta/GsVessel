package com.example.GSVessel.Repository;

import com.example.GSVessel.Model.Equipment;
import com.example.GSVessel.Model.Enums.EquipmentCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// ¡NO se necesita @Repository en interfaces de Spring Data!
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    // Buscar por barco
    List<Equipment> findByShipId(Long shipId);

    // Buscar por categoría
    List<Equipment> findByCategory(EquipmentCategory category);
}