package com.example.csTraining.controller.models;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagoDTO {
    private Long id;
    private Double monto;
    private String fechaPago;
    private Long userId;

    public PagoDTO(Long id, Double monto, LocalDate fechaPago, Long userId) {
        this.id = id;
        this.monto = monto;
        this.fechaPago = fechaPago.toString();
        this.userId = userId;
    }
}
