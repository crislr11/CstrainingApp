package com.example.csTraining.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Class {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Enumerated(EnumType.STRING)
    private Oposion oposicion;  // Tipo de oposición

    @ManyToMany
    @JoinTable(
            name = "class_profesores",
            joinColumns = @JoinColumn(name = "class_id"),
            inverseJoinColumns = @JoinColumn(name = "profesor_id")
    )
    private List<User> profesores;  // Máximo 2 profesores

    @ManyToMany
    @JoinTable(
            name = "class_alumnos",
            joinColumns = @JoinColumn(name = "class_id"),
            inverseJoinColumns = @JoinColumn(name = "alumno_id")
    )
    private List<User> alumnos;  // Lista de alumnos (opositores)

    private LocalDateTime fecha;  // Fecha y hora de la clase
}
