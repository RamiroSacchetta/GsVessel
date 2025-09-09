package com.example.GSVessel.Model;

import com.example.GSVessel.Model.Enums.TipoMaintenance;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "maintenance")
public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La fecha es obligatoria")
    @NotNull
    private LocalDate fecha;

    @NotNull(message = "Es obligatorio ingresar el costo")
    @Column(precision = 10, scale = 2)
    private BigDecimal costo;

    private String descripcion;

    @NotNull
    @NotBlank(message = "Es obligatorio ingresar el tipo de mantenimiento")
    private TipoMaintenance tipoMaintenance;

    // Relación con Equipment
    @ManyToOne
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoMaintenance getTipoMaintenance() {
        return tipoMaintenance;
    }

    public void setTipoMaintenance(TipoMaintenance tipoMaintenance) {
        this.tipoMaintenance = tipoMaintenance;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public Maintenance(Long id, LocalDate fecha, BigDecimal costo, String descripcion, TipoMaintenance tipoMaintenance, Equipment equipment) {
        this.id = id;
        this.fecha = fecha;
        this.costo = costo;
        this.descripcion = descripcion;
        this.tipoMaintenance = tipoMaintenance;
        this.equipment = equipment;
    }
}
