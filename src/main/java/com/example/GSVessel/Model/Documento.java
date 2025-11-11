package com.example.GSVessel.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "documentos")
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El nombre del documento es obligatorio")
    private String nombre;

    @NotNull(message = "El tipo de documento es obligatorio")
    @Enumerated(EnumType.STRING)
    private TipoDocumento tipo; // Ej: CERTIFICADO_MATRICULA, SEGURO, etc.

    private String descripcion;

    @Column(length = 500)
    private String url; // URL del archivo en Cloudinary o disco

    private LocalDate fechaExpiracion; // Opcional: para certificados con vencimiento

    private LocalDate fechaSubida; // Automático: cuando se sube

    // Relación con entidades
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barco_id")
    private Ship barco;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tripulante_id")
    private Tripulante tripulante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mantenimiento_id")
    private Maintenance mantenimiento;

    // Campo calculado (no persistido)
    @Transient
    public boolean isVencido() {
        return this.fechaExpiracion != null && this.fechaExpiracion.isBefore(LocalDate.now());
    }
}