package com.example.GSVessel.Model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

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

    private Double foodExpense;        // Gasto en comida
    private Double shacklesExpense;    // Gasto en grilletes
    private Double cablesExpense;      // Gasto en cables
    private Double otherExpenses;      // Otros gastos

    private int shacklesUsed;          // Grilletes usados
    private int cargoUsed;             // Carga usada

    // Relación con el barco
    @ManyToOne
    @JoinColumn(name = "ship_id", nullable = false)
    private Ship ship;
}

