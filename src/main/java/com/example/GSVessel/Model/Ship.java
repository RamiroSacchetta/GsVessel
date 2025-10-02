package com.example.GSVessel.Model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String registration;
    private int hoursUsed;
    private boolean active;
    private int crewSize;
    private int cargoCrates;
    private int numberOfNets;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "ship", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Equipment> equipment;

    @OneToMany(mappedBy = "ship", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Trip> trips;

    // Getters y Setters manuales
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRegistration() { return registration; }
    public void setRegistration(String registration) { this.registration = registration; }

    public int getHoursUsed() { return hoursUsed; }
    public void setHoursUsed(int hoursUsed) { this.hoursUsed = hoursUsed; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public int getCrewSize() { return crewSize; }
    public void setCrewSize(int crewSize) { this.crewSize = crewSize; }

    public int getCargoCrates() { return cargoCrates; }
    public void setCargoCrates(int cargoCrates) { this.cargoCrates = cargoCrates; }

    public int getNumberOfNets() { return numberOfNets; }
    public void setNumberOfNets(int numberOfNets) { this.numberOfNets = numberOfNets; }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }

    public List<Equipment> getEquipment() { return equipment; }
    public void setEquipment(List<Equipment> equipment) { this.equipment = equipment; }

    public List<Trip> getTrips() { return trips; }
    public void setTrips(List<Trip> trips) { this.trips = trips; }
}