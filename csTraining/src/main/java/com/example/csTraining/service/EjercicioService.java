package com.example.csTraining.service;

import com.example.csTraining.entity.Ejercicio;

import java.util.List;

public interface EjercicioService {

    // Crear un nuevo ejercicio
    Ejercicio crearEjercicio(Ejercicio ejercicio);

    // Obtener todos los ejercicios
    List<Ejercicio> obtenerTodosEjercicios();

    // Obtener ejercicios por simulacro
    List<Ejercicio> obtenerEjerciciosPorSimulacro(Long idSimulacro);

    // Modificar el nombre de un ejercicio
    Ejercicio modificarNombreEjercicio(Long id, String nuevoNombre);

    // Modificar la marca de un ejercicio
    Ejercicio modificarMarcaEjercicio(Long id, double nuevaMarca);

    // Eliminar un ejercicio
    void eliminarEjercicio(Long id);
}
