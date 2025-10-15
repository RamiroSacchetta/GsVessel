package com.example.GSVessel.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipDTO {

    private String name;
    private String registration;
    private boolean active;
    private int crewSize;
    private String barcoNombre; // el nombre del barco al que se asocia
}
