package com.example.csTraining.controller.DTO.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntrenamientoInscripcionResponse {
    private String message;
    private Long entrenamientoId;
    private Long userId;
    private boolean inscrito;
    private int creditosRestantes;


}
