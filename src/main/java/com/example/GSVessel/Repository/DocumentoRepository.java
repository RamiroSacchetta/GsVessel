package com.example.GSVessel.Repository;

import com.example.GSVessel.Model.CrewMember;
import com.example.GSVessel.Model.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {

    List<Documento> findByBarcoIdOrderByFechaExpiracionAsc(Long barcoId);
    List<Documento> findByEquipmentIdOrderByFechaExpiracionAsc(Long equipmentId);
    List<Documento> findByCrewMemberIdOrderByFechaExpiracionAsc(Long crewMemberId);
    List<Documento> findByMantenimientoIdOrderByFechaExpiracionAsc(Long mantenimientoId);

    // Buscar todos ordenados por fecha de expiración (más próximo a más lejano)
    @Query("SELECT d FROM Documento d ORDER BY d.fechaExpiracion ASC NULLS LAST")
    List<Documento> findAllOrderByFechaExpiracionAsc();

    // Buscar documentos vencidos
    @Query("SELECT d FROM Documento d WHERE d.fechaExpiracion IS NOT NULL AND d.fechaExpiracion < :fecha")
    List<Documento> findExpiredDocumentsBefore(LocalDate fecha);

    // Buscar documentos por vencer (dentro de X días)
    @Query("SELECT d FROM Documento d WHERE d.fechaExpiracion IS NOT NULL AND d.fechaExpiracion BETWEEN :fechaInicio AND :fechaFin")
    List<Documento> findDocumentsExpiringBetween(LocalDate fechaInicio, LocalDate fechaFin);
}