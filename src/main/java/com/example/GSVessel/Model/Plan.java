package com.example.GSVessel.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "plans")
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    private Double precio;

    private String descripcion;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL)
    private List<com.example.GSVessel.Model.User> usuarios;
}
