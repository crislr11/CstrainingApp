package com.example.csTraining.service;

import com.example.csTraining.entity.Ejercicio;
import com.example.csTraining.entity.Simulacro;

import java.util.List;

public interface SimulacroService {
    Simulacro crearSimulacro(Long userId, Simulacro simulacro);
    List<Simulacro> obtenerSimulacrosDeUsuario(Long userId);
    Simulacro obtenerSimulacroPorId(Long id);
    void eliminarSimulacro(Long id);
    public Ejercicio añadirEjercicioASimulacro(Long id, Ejercicio ejercicio);
    public void eliminarEjercicioDeSimulacro(Long simulacroId, Long ejercicioId);
    public List<Ejercicio> obtenerEjerciciosDeSimulacro(Long id);

}
