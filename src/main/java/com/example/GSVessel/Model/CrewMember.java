package com.example.GSVessel.Model;

import jakarta.persistence.*;

@Entity
public class CrewMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String role;

    private String contact;

    @ManyToOne
    @JoinColumn(name = "ship_id")
    private Ship ship;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public Ship getShip() { return ship; }
    public void setShip(Ship ship) { this.ship = ship; }
}
