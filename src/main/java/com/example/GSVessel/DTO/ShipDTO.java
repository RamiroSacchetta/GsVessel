package com.example.GSVessel.DTO;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipDTO {

    private Long id;
    private String name;
    private String registration;
    private boolean active;
    private int crewSize;
    private Long ownerId;
}
