package com.example.GSVessel.Model;

import com.example.GSVessel.Model.Enums.EquipmentCategory;
import com.example.GSVessel.Model.Enums.EquipmentLocation;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "equipment")
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EquipmentCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EquipmentLocation location;

    @Column(nullable = false)
    private Double consumption;

    @Column(nullable = false)
    private int hoursUsed;

    private Double budget;

    private String description;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "ship_id")
    private Ship ship;

    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Maintenance> mantenimientos = new ArrayList<>();

    // Constructores
    public Equipment() {}

    public Equipment(String name, EquipmentCategory category, EquipmentLocation location,
                     Double consumption, int hoursUsed, Double budget,
                     String description, String imageUrl, Ship ship) {
        this.name = name;
        this.category = category;
        this.location = location;
        this.consumption = consumption;
        this.hoursUsed = hoursUsed;
        this.budget = budget;
        this.description = description;
        this.imageUrl = imageUrl;
        this.ship = ship;
    }

    // Getters y Setters
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

    public Ship getShip() { return ship; }
    public void setShip(Ship ship) { this.ship = ship; }

    public List<Maintenance> getMantenimientos() { return mantenimientos; }
    public void setMantenimientos(List<Maintenance> mantenimientos) { this.mantenimientos = mantenimientos; }

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Equipment)) return false;
        Equipment that = (Equipment) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}