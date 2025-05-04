package com.example.csTraining.service;

import java.util.List;

public interface SimulacroService {
    Simulacro crearSimulacro(Long userId, Simulacro simulacro);
    List<Simulacro> obtenerSimulacrosDeUsuario(Long userId);
    void eliminarSimulacro(Long id);
    List<Ejercicio> obtenerEjerciciosDeSimulacro(Long simulacroId);
    Simulacro obtenerSimulacroPorId(Long id);
}
