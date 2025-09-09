package com.example.GSVessel.Model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @OneToMany(mappedBy = "ship", cascade = CascadeType.ALL)
    private List<Equipment> equipment;

    @OneToMany(mappedBy = "ship", cascade = CascadeType.ALL)
    private List<Trip> trips;

    /*@OneToMany(mappedBy = "ship", cascade = CascadeType.ALL)
    private List<CorrectiveMaintenance> correctiveMaintenances;

    @OneToMany(mappedBy = "ship", cascade = CascadeType.ALL)
    private List<PreventiveMaintenance> preventiveMaintenances;

    @OneToMany(mappedBy = "ship", cascade = CascadeType.ALL)
    private List<StockItem> stockItems;
*/
}



