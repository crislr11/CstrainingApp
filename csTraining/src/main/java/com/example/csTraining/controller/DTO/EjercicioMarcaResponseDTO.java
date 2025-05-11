package com.example.csTraining.controller.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EjercicioMarcaResponseDTO {
    private Long id;
    private Double marca;
    private Long simulacroId;
    private Long ejercicioId;
    private String nombre;
}

