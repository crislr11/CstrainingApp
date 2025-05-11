package com.example.csTraining.controller.DTO;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimulacroResponseDTO {
    private Long id;
    private String titulo;
    private LocalDate fecha;
    private Long userId;
    private List<EjercicioMarcaResponseDTO> ejercicios; // opcionalmente puede omitirse si no es necesario
}
