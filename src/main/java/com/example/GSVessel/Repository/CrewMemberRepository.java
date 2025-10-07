package com.example.GSVessel.Repository;

import com.example.GSVessel.Model.CrewMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrewMemberRepository extends JpaRepository<CrewMember, Long> {

    // Obtener todos los tripulantes de un barco
    List<CrewMember> findByShipId(Long shipId);

}
