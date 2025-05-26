package com.example.csTraining.service.simulacro;


import com.example.csTraining.controller.DTO.response.EjercicioRequestDTO;
import com.example.csTraining.controller.DTO.response.EjercicioResponseDTO;
import com.example.csTraining.entity.simulacros.Ejercicio;

import java.util.List;

public interface EjercicioService {


    Ejercicio getEjercicioPorNombre(String nombre);
    Ejercicio saveEjercicio(Ejercicio ejercicio);
    void eliminarEjercicio(Long id);
    List<Ejercicio> getAllEjercicios();

    EjercicioResponseDTO buscarEjercicioPorNombre(String nombre);
    EjercicioResponseDTO guardarEjercicio(EjercicioRequestDTO ejercicioDTO);
    List<EjercicioResponseDTO> listarTodosEjercicios();
}