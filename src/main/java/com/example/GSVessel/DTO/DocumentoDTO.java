package com.example.GSVessel.DTO;

import com.example.GSVessel.Model.Enums.TipoDocumento;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public class DocumentoDTO {

    private Long id;
    private String nombre;
    private TipoDocumento tipo;
    private String descripcion;
    private String url;
    private LocalDate fechaExpiracion;
    private LocalDate fechaSubida;

    // Campos para asociar
    private Long barcoId;
    private Long equipmentId;
    private Long tripulanteId;
    private Long mantenimientoId;

    // Solo para subida
    private MultipartFile archivo;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public TipoDocumento getTipo() { return tipo; }
    public void setTipo(TipoDocumento tipo) { this.tipo = tipo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public LocalDate getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(LocalDate fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }

    public LocalDate getFechaSubida() { return fechaSubida; }
    public void setFechaSubida(LocalDate fechaSubida) { this.fechaSubida = fechaSubida; }

    public Long getBarcoId() { return barcoId; }
    public void setBarcoId(Long barcoId) { this.barcoId = barcoId; }

    public Long getEquipmentId() { return equipmentId; }
    public void setEquipmentId(Long equipmentId) { this.equipmentId = equipmentId; }

    public Long getTripulanteId() { return tripulanteId; }
    public void setTripulanteId(Long tripulanteId) { this.tripulanteId = tripulanteId; }

    public Long getMantenimientoId() { return mantenimientoId; }
    public void setMantenimientoId(Long mantenimientoId) { this.mantenimientoId = mantenimientoId; }

    public MultipartFile getArchivo() { return archivo; }
    public void setArchivo(MultipartFile archivo) { this.archivo = archivo; }
}