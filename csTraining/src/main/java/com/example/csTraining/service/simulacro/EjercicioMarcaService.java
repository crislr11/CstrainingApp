package com.example.csTraining.service.simulacro;

import com.example.csTraining.controller.DTO.response.EjercicioMarcaRequestDTO;
import com.example.csTraining.controller.DTO.response.EjercicioMarcaResponseDTO;

import java.util.List;

public interface EjercicioMarcaService {

    // Guardar una marca
    EjercicioMarcaResponseDTO guardarMarca(EjercicioMarcaRequestDTO dto);

    // Obtener todas las marcas de un ejercicio por ID
    List<EjercicioMarcaResponseDTO> listarMarcasPorEjercicio(Long ejercicioId);

    // Obtener el top 5 marcas de un ejercicio
    List<EjercicioMarcaResponseDTO> listarTop5PorEjercicio(Long ejercicioId);

    // Obtener todas las marcas por simulacro
    List<EjercicioMarcaResponseDTO> listarMarcasPorSimulacro(Long simulacroId);

    // Obtener ranking de un usuario por ejercicio
    List<EjercicioMarcaResponseDTO> obtenerRankingUsuario(Long userId, String ejercicioNombre);

    // Eliminar una marca por ID
    void eliminarMarca(Long id);
}
