package com.example.GSVessel.DTO;

import com.example.GSVessel.Model.Enums.TipoMaintenance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceDTO {

    private Long id;
    private LocalDate fecha;
    private BigDecimal costo;
    private String Descripcion;
    private TipoMaintenance tipoMaintenance;

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
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public TipoMaintenance getTipoMaintenance() {
        return tipoMaintenance;
    }

    public void setTipoMaintenance(TipoMaintenance tipoMaintenance) {
        this.tipoMaintenance = tipoMaintenance;
    }

    public MaintenanceDTO(Long id, LocalDate fecha, BigDecimal costo, String descripcion, TipoMaintenance tipoMaintenance) {
        this.id = id;
        this.fecha = fecha;
        this.costo = costo;
        Descripcion = descripcion;
        this.tipoMaintenance = tipoMaintenance;
    }
}
