package com.example.GSVessel.Repository;

import com.example.GSVessel.Model.Barco;
import com.example.GSVessel.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BarcoRepository extends JpaRepository<Barco, Long> {
    Optional<Barco> findByOwnerAndNombre(User owner, String nombre);
}
