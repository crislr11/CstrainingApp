package com.example.csTraining.controller.DTO.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EjercicioMarcaRequestDTO {
    private Double marca;
    private Long simulacroId;
    private Long ejercicioId;
    private String nombre;
}
