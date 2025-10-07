package com.example.GSVessel.DTO;

public class CrewMemberDTO {

    private Long id;
    private String name;
    private String role;
    private String contact;
    private Long shipId;

    public CrewMemberDTO() {
    }

    public CrewMemberDTO(Long id, String name, String role, String contact, Long shipId) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.contact = contact;
        this.shipId = shipId;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Long getShipId() {
        return shipId;
    }

    public void setShipId(Long shipId) {
        this.shipId = shipId;
    }

}
