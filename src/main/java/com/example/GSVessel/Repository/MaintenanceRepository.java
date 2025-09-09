package com.example.GSVessel.Repository;

import com.example.GSVessel.Model.Enums.TipoMaintenance;
import com.example.GSVessel.Model.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaintenanceRepository extends JpaRepository <Maintenance,Long> {

    List<Maintenance> findByTipoMaintenance (TipoMaintenance tipoMaintenance);
}
