package com.example.csTraining.service;

import java.util.List;

public interface EjercicioService {
    Ejercicio añadirEjercicioASimulacro(Long simulacroId, Ejercicio ejercicio);
    Ejercicio editarEjercicio(Long id, Ejercicio ejercicio);
    void eliminarEjercicio(Long id);
    List<Ejercicio> obtenerEjerciciosPorSimulacro(Long simulacroId); // Nuevo método
}
