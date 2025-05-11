package com.example.csTraining.controller.DTO;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimulacroRequestDTO {
    private String titulo;
    private LocalDate fecha;
    private Long userId;
    private List<EjercicioMarcaRequestDTO> ejercicios;  // Cambié a DTO de EjercicioMarca
}
