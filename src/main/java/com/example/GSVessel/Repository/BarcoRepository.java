package com.example.GSVessel.Repository;

import com.example.GSVessel.Model.Barco;
import com.example.GSVessel.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BarcoRepository extends JpaRepository<Barco, Long> { ;
    List<Barco> findByOwnerId(Long userId);
}
