package com.example.GSVessel.DTO;

import java.time.LocalDate;

public class MantenimientoPreventivoDTO {

    private Long id;
    private Long horasCambio;
    private Long horasAviso;
    private LocalDate fechaUltimoCambio;
    private Long equipmentId; // Solo el ID del equipo
    private boolean alerta; // Calculado en el servicio

    public MantenimientoPreventivoDTO() {}

    // Constructor con parámetros
    public MantenimientoPreventivoDTO(Long id, Long horasCambio, Long horasAviso, LocalDate fechaUltimoCambio, Long equipmentId, boolean alerta) {
        this.id = id;
        this.horasCambio = horasCambio;
        this.horasAviso = horasAviso;
        this.fechaUltimoCambio = fechaUltimoCambio;
        this.equipmentId = equipmentId;
        this.alerta = alerta;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getHorasCambio() { return horasCambio; }
    public void setHorasCambio(Long horasCambio) { this.horasCambio = horasCambio; }

    public Long getHorasAviso() { return horasAviso; }
    public void setHorasAviso(Long horasAviso) { this.horasAviso = horasAviso; }

    public LocalDate getFechaUltimoCambio() { return fechaUltimoCambio; }
    public void setFechaUltimoCambio(LocalDate fechaUltimoCambio) { this.fechaUltimoCambio = fechaUltimoCambio; }

    public Long getEquipmentId() { return equipmentId; }
    public void setEquipmentId(Long equipmentId) { this.equipmentId = equipmentId; }

    public boolean isAlerta() { return alerta; }
    public void setAlerta(boolean alerta) { this.alerta = alerta; }
}