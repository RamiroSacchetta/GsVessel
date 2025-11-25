package com.example.GSVessel.Repository;

import com.example.GSVessel.Model.Enums.Role;
import com.example.GSVessel.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmailIgnoreCase(String email);
    Optional<User> findByResetPasswordToken(String token);

    // consulta para traer empleados por owner id
    @Query("select u from User u where u.owner.id = :ownerId and u.role = :role")
    List<User> findByOwnerIdAndRole(@Param("ownerId") Long ownerId, @Param("role") Role role);

    // si querés también traer todos los users de un owner (sin filtrar por role)
    @Query("select u from User u where u.owner.id = :ownerId")
    List<User> findByOwnerId(@Param("ownerId") Long ownerId);
    

    boolean existsByEmail(String email);

}
