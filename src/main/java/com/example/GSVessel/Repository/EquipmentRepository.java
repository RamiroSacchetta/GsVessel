package com.example.GSVessel.Repository;

import com.example.GSVessel.Model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    // Buscar por barco
    List<Equipment> findByShipId(Long shipId);

    // Buscar por categoría
    List<Equipment> findByCategory(com.example.GSVessel.Model.Enums.EquipmentCategory category);
}

