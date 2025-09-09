package com.example.GSVessel.Model;

import com.example.GSVessel.Model.Enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El username no puede estar vacío")
    @Size(min = 3, max = 50, message = "El username debe tener entre 3 y 50 caracteres")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe ser válido")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;


@ManyToOne
    @JoinColumn(name = "plan_id")
    private com.example.gsvessel.model.Plan plan;


    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Barco> barcos;

    @Column(nullable = false)
    private boolean enabled = false;

}
