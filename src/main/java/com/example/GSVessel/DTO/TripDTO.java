package com.example.GSVessel.DTO;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripDTO {
    private Long id;
    private LocalDate departureDate;
    private LocalDate returnDate;

    private Double otherExpenses;


    private Long shipId; // Solo el ID del barco en el DTO
}

