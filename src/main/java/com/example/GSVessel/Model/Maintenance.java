package com.example.GSVessel.Model;

import com.example.GSVessel.DTO.MaintenanceDTO;
import com.example.GSVessel.Model.Enums.TipoMaintenance;
import jakarta.persistence.*;
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

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @Column(precision = 10, scale = 2)
    private BigDecimal costo;

    private String descripcion;

    @NotNull(message = "Es obligatorio ingresar el tipo de mantenimiento")
    @Enumerated(EnumType.STRING)
    private TipoMaintenance tipoMaintenance;

    // Relación con Equipment
    @ManyToOne
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    @Column(name = "taller")
    private String taller;



    private String imageUrl;
}
