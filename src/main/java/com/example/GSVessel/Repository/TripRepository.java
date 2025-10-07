package com.example.GSVessel.Repository;

import com.example.GSVessel.Model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByShipId(Long shipId); // Buscar viajes por barco
}

