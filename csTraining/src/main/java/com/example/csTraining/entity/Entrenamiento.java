package com.example.csTraining.entity;

import com.example.csTraining.entity.enums.Lugar;
import com.example.csTraining.entity.enums.Oposicion;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "entrenamientos")
public class Entrenamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Oposicion oposicion;

    @ManyToMany
    @JoinTable(
            name = "entrenamiento_profesores",
            joinColumns = @JoinColumn(name = "entrenamiento_id"),
            inverseJoinColumns = @JoinColumn(name = "profesor_id")
    )

    @JsonBackReference
    private List<User> profesores;

    @ManyToMany
    @JoinTable(
            name = "entrenamiento_alumnos",
            joinColumns = @JoinColumn(name = "entrenamiento_id"),
            inverseJoinColumns = @JoinColumn(name = "alumno_id")
    )
    @JsonBackReference
    private List<User> alumnos;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Lugar lugar;

    @PrePersist
    @PreUpdate
    private void validarProfesores() {
        if (profesores != null && profesores.size() > 2) {
            throw new IllegalStateException("Un entrenamiento no puede tener más de 2 profesores.");
        }
    }
}