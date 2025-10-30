package com.example.GSVessel.Repository;

import com.example.GSVessel.Model.Barco;
import com.example.GSVessel.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BarcoRepository extends JpaRepository<Barco, Long> {

    // Busca un barco por su propietario y nombre
    Optional<Barco> findByOwnerAndNombre(User owner, String nombre);

    // Cuenta los barcos activos e inactivos del usuario (owner)
    Long countByOwnerIdAndActivoTrue(Long ownerId);
    Long countByOwnerIdAndActivoFalse(Long ownerId);
}
