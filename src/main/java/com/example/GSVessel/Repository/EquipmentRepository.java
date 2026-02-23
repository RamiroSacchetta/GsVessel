package com.example.GSVessel.Repository;

import com.example.GSVessel.Model.Equipment;
import com.example.GSVessel.Model.Enums.EquipmentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    List<Equipment> findByShipId(Long shipId);

    List<Equipment> findByCategory(EquipmentCategory category);

    // Cargar equipo con su jerarquía completa (hasta 3 niveles de profundidad)
    @Query("SELECT e FROM Equipment e " +
            "LEFT JOIN FETCH e.children c1 " +
            "LEFT JOIN FETCH c1.children c2 " +
            "LEFT JOIN FETCH c2.children c3 " +
            "WHERE e.id = :id")
    Optional<Equipment> findWithHierarchyById(@Param("id") Long id);

    // Obtener raíces de un barco (equipos sin padre)
    List<Equipment> findByShipIdAndParentIsNull(Long shipId);
}