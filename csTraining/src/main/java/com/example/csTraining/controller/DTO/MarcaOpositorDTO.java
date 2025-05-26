package com.example.csTraining.controller.DTO;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MarcaOpositorDTO {
    private Long userId;
    private Long ejercicioId;
    private Double valor;
    private LocalDateTime fecha;
}