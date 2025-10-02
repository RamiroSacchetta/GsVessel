package com.example.GSVessel.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "barcos")
public class Barco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String tipo; // Por ejemplo: "Velero", "Yate", "Lancha", etc.

    private Double eslora; // Longitud del barco en metros

    private Double manga; // Ancho del barco

    private Double calado; // Profundidad del barco

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;


}
