package com.example.csTraining.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Ejercicios")
public class Ejercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreEjercicio;
    private double marca;

    @ManyToOne
    @JoinColumn(name = "simulacro_id")
    private Simulacro simulacro;
}

