package com.example.csTraining.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarcaOpositor {
    private Long ejercicioId;
    private Double valor;
    private LocalDateTime fecha;
}