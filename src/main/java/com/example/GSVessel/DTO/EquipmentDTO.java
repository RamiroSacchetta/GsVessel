package com.example.GSVessel.DTO;

import com.example.GSVessel.Model.Enums.EquipmentCategory;
import com.example.GSVessel.Model.Enums.EquipmentLocation;

public class EquipmentDTO {

    private Long id;
    private String name;
    private EquipmentCategory category;
    private EquipmentLocation location;
    private Double consumption;
    private int hoursUsed;
    private Double budget;
    private String description;
    private String imageUrl;
    private Long shipId;

    public EquipmentDTO() {}

    public EquipmentDTO(Long id, String name, EquipmentCategory category, EquipmentLocation location,
                        Double consumption, int hoursUsed, Double budget, String description, String imageUrl, Long shipId) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.location = location;
        this.consumption = consumption;
        this.hoursUsed = hoursUsed;
        this.budget = budget;
        this.description = description;
        this.imageUrl = imageUrl;
        this.shipId = shipId;
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public EquipmentCategory getCategory() { return category; }
    public void setCategory(EquipmentCategory category) { this.category = category; }

    public EquipmentLocation getLocation() { return location; }
    public void setLocation(EquipmentLocation location) { this.location = location; }

    public Double getConsumption() { return consumption; }
    public void setConsumption(Double consumption) { this.consumption = consumption; }

    public int getHoursUsed() { return hoursUsed; }
    public void setHoursUsed(int hoursUsed) { this.hoursUsed = hoursUsed; }

    public Double getBudget() { return budget; }
    public void setBudget(Double budget) { this.budget = budget; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Long getShipId() { return shipId; }
    public void setShipId(Long shipId) { this.shipId = shipId; }
}
