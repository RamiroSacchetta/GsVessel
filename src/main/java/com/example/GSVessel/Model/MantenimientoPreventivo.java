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
@Table(name = "mantenimiento_preventivo")
public class MantenimientoPreventivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Las horas de cambio son obligatorias")
    @Column(name = "horas_cambio", nullable = false)
    private Long horasCambio; // Ej: 500 hs

    @NotNull(message = "Las horas para aviso son obligatorias")
    @Column(name = "horas_aviso", nullable = false)
    private Long horasAviso; // Ej: 50 hs antes

    @NotNull(message = "La fecha del último cambio es obligatoria")
    @Column(name = "fecha_ultimo_cambio", nullable = false)
    private LocalDate fechaUltimoCambio; // Se actualiza al reiniciar

    // Relación con Equipment (la parte específica del barco)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment; // <-- Reutiliza la entidad existente

    // Campo calculado (no persistido) para saber si hay alerta
    @Transient
    public boolean isAlerta() {
        // Calcula si las horas actuales del equipo superan el umbral de aviso
        long horasActuales = this.equipment.getHoursUsed();
        long horasHastaCambio = this.horasCambio - this.horasAviso;
        return horasActuales >= horasHastaCambio;
    }
}