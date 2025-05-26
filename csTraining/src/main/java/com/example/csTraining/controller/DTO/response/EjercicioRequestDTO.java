package com.example.csTraining.controller.DTO.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EjercicioRequestDTO {
    private Long id;
    private String nombre;
    private String descripcion;
}