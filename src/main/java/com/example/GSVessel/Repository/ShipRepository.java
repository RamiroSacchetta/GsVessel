package com.example.GSVessel.Repository;

import com.example.GSVessel.Model.Ship;
import com.example.GSVessel.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShipRepository extends JpaRepository<Ship, Long> {

    List<Ship> findAllByBarcoOwner(User owner);

    Optional<Ship> findByIdAndBarcoOwner(Long id, User owner);
}
