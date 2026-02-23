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
    private Long equipmentId;
    private String equipmentName;
    private Long shipId;
    private String shipName;
    private String taller;

    private MaintenanceDTO() {}

    public Long getId() { return id; }
    public LocalDate getFecha() { return fecha; }
    public BigDecimal getCosto() { return costo; }
    public String getDescripcion() { return descripcion; }
    public TipoMaintenance getTipoMaintenance() { return tipoMaintenance; }
    public MultipartFile getImage() { return image; }
    public String getImageUrl() { return imageUrl; }
    public Long getEquipmentId() { return equipmentId; }
    public String getEquipmentName() { return equipmentName; }
    public Long getShipId() { return shipId; }
    public String getShipName() { return shipName; }
    public String getTaller() { return taller; }

    public void setEquipmentName(String equipmentName) { this.equipmentName = equipmentName; }
    public void setShipId(Long shipId) { this.shipId = shipId; }
    public void setShipName(String shipName) { this.shipName = shipName; }
    public void setTaller(String taller) { this.taller = taller; }

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
        private String imageUrl;
        private Long equipmentId;
        private String equipmentName;
        private Long shipId;
        private String shipName;
        private String taller;

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

        public MaintenanceDTOBuilder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public MaintenanceDTOBuilder equipmentId(Long equipmentId) {
            this.equipmentId = equipmentId;
            return this;
        }

        public MaintenanceDTOBuilder equipmentName(String equipmentName) {
            this.equipmentName = equipmentName;
            return this;
        }

        public MaintenanceDTOBuilder shipId(Long shipId) {
            this.shipId = shipId;
            return this;
        }

        public MaintenanceDTOBuilder shipName(String shipName) {
            this.shipName = shipName;
            return this;
        }

        public MaintenanceDTOBuilder taller(String taller) {
            this.taller = taller;
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
            dto.equipmentId = this.equipmentId;
            dto.equipmentName = this.equipmentName;
            dto.shipId = this.shipId;
            dto.shipName = this.shipName;
            dto.taller = this.taller;
            return dto;
        }
    }
}
