package com.example.csTraining.entity.simulacros;

import com.example.csTraining.entity.User;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "simulacros")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Simulacro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private LocalDate fecha;

    @ManyToOne(optional = true)
    @JoinColumn(name = "user_id")
    @JsonManagedReference  // Cambié aquí a JsonManagedReference
    private User user;

    @OneToMany(mappedBy = "simulacro", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<EjercicioMarca> ejercicios = new ArrayList<>();
}
