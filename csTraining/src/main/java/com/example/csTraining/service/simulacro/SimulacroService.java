package com.example.csTraining.service.simulacro;

import com.example.csTraining.controller.DTO.response.EjercicioMarcaRequestDTO;
import com.example.csTraining.controller.DTO.response.SimulacroRequestDTO;
import com.example.csTraining.controller.DTO.response.SimulacroResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface SimulacroService {

    // Obtener todos los simulacros de un usuario
    List<SimulacroResponseDTO> getSimulacrosPorUsuario(Long userId);

    // Crear un simulacro
    SimulacroResponseDTO saveSimulacro(SimulacroRequestDTO simulacroDTO);

    // Obtener simulacros entre dos fechas
    List<SimulacroResponseDTO> getSimulacrosPorFecha(LocalDate startDate, LocalDate endDate);

    // Eliminar un simulacro
    void eliminarSimulacro(Long id);

    // Asignar simulacro a usuario
    SimulacroResponseDTO asignarSimulacroAUsuario(Long simulacroId, Long userId);

    // Agregar ejercicio a simulacro
    SimulacroResponseDTO agregarEjercicioASimulacro(Long simulacroId, EjercicioMarcaRequestDTO nuevoEjercicio);
}
