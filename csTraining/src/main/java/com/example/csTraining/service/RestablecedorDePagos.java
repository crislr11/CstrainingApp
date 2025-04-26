package com.example.csTraining.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class RestablecedorDePagos {

    private final PagoService pagoService;

    public RestablecedorDePagos(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @Scheduled(cron = "0 0 23 30,31 * ?")  // Ejecutar a las 23:00 del 30 y 31 de cada mes
    public void ejecutarRestablecimientoDePagos() {
        pagoService.restablecerEstadoPagado();
    }
}
