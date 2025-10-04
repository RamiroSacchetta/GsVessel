package com.example.GSVessel.DTO;

import com.example.GSVessel.Model.Enums.TipoMaintenance;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MaintenanceDTO {
    private Long id;
    private LocalDate fecha;
    private BigDecimal costo;
    private String descripcion;
    private TipoMaintenance tipoMaintenance;
    private MultipartFile image;      // Solo usado en entrada (subida)
    private String imageUrl;          // Solo usado en salida (URL de Cloudinary)

    // Constructor privado
    private MaintenanceDTO() {}

    // Getters
    public Long getId() { return id; }
    public LocalDate getFecha() { return fecha; }
    public BigDecimal getCosto() { return costo; }
    public String getDescripcion() { return descripcion; }
    public TipoMaintenance getTipoMaintenance() { return tipoMaintenance; }
    public MultipartFile getImage() { return image; }
    public String getImageUrl() { return imageUrl; } // ← Nuevo getter

    // Builder estático
    public static MaintenanceDTOBuilder builder() {
        return new MaintenanceDTOBuilder();
    }

    public static class MaintenanceDTOBuilder {
        private Long id;
        private LocalDate fecha;
        private BigDecimal costo;
        private String descripcion;
        private TipoMaintenance tipoMaintenance;
        private MultipartFile image;
        private String imageUrl; // ← Nuevo campo en el builder

        public MaintenanceDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public MaintenanceDTOBuilder fecha(LocalDate fecha) {
            this.fecha = fecha;
            return this;
        }

        public MaintenanceDTOBuilder costo(BigDecimal costo) {
            this.costo = costo;
            return this;
        }

        public MaintenanceDTOBuilder descripcion(String descripcion) {
            this.descripcion = descripcion;
            return this;
        }

        public MaintenanceDTOBuilder tipoMaintenance(TipoMaintenance tipoMaintenance) {
            this.tipoMaintenance = tipoMaintenance;
            return this;
        }

        public MaintenanceDTOBuilder image(MultipartFile image) {
            this.image = image;
            return this;
        }

        // Nuevo método en el builder
        public MaintenanceDTOBuilder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public MaintenanceDTO build() {
            MaintenanceDTO dto = new MaintenanceDTO();
            dto.id = this.id;
            dto.fecha = this.fecha;
            dto.costo = this.costo;
            dto.descripcion = this.descripcion;
            dto.tipoMaintenance = this.tipoMaintenance;
            dto.image = this.image;
            dto.imageUrl = this.imageUrl;
            return dto;
        }
    }
}