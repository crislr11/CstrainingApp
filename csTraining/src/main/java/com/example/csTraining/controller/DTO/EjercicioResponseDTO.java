package com.example.csTraining.controller.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EjercicioResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
}