package com.example.GSVessel.Model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate departureDate;   // Fecha de salida
    private LocalDate returnDate;      // Fecha de regreso

    private Double otherExpenses;      // Otros gastos (opcionales)

    @ManyToOne
    @JoinColumn(name = "ship_id", nullable = false)
    private Ship ship;

    // Relación con los items usados del stock
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripStockItem> usedItems = new ArrayList<>();
}
