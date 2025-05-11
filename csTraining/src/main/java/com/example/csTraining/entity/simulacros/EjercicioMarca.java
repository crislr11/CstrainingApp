package com.example.csTraining.entity.simulacros;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "ejercicio_marcas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EjercicioMarca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double marca;

    @ManyToOne
    @JoinColumn(name = "simulacro_id")
    @JsonBackReference
    private Simulacro simulacro;

    @ManyToOne
    @JoinColumn(name = "ejercicio_id")
    @JsonBackReference
    private Ejercicio ejercicio;

    private String nombre;
}
