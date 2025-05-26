package com.example.csTraining.service;


import com.example.csTraining.controller.DTO.EntrenamientoDTO;
import com.example.csTraining.controller.DTO.response.EntrenamientoResponseDTO;
import com.example.csTraining.entity.enums.Oposicion;
import com.example.csTraining.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface EntrenamientoService {

    // Crea un nuevo entrenamiento (solo el ADMIN puede hacerlo)
    EntrenamientoResponseDTO createTraining(EntrenamientoDTO trainingDTO);

    // Elimina un entrenamiento existente (ADMIN y PROFESOR pueden hacerlo, según permisos)
    void deleteTraining(Long id, User user);

    // Actualiza un entrenamiento existente (ADMIN puede actualizar cualquiera, PROFESOR solo los suyos)
    EntrenamientoResponseDTO updateTraining(Long id, EntrenamientoDTO trainingDTO, User user);

    // Devuelve la lista de todos los entrenamientos (solo accesible para ADMIN)
    List<EntrenamientoResponseDTO> getAllTrainings();

    // Devuelve los entrenamientos asociados a un profesor específico (ADMIN y PROFESOR)
    List<EntrenamientoResponseDTO> getTrainingsByProfessor(Long professorId);

    // Devuelve los entrenamientos disponibles para una oposición específica (ADMIN y OPOSITOR)
    List<EntrenamientoResponseDTO> getTrainingsByOpposition(Oposicion opposition);

    // Devuelve un entrenamiento específico por su ID (ADMIN, PROFESOR y OPOSITOR pueden acceder)
    EntrenamientoResponseDTO getTrainingById(Long id);

    // Devuelve los entrenamientos futuros asociados a una oposición (OPOSITOR)
    List<EntrenamientoResponseDTO> getFutureTrainingsByOpposition(Oposicion opposition, LocalDateTime fechaReferencia);

    // Devuelve los entrenamientos futuros asignados a un profesor (PROFESOR)
    List<EntrenamientoResponseDTO> getFutureTrainingsByProfessor(Long professorId);

    // Devuelve los entrenamientos dentro de un rango de fechas (ADMIN)
    List<EntrenamientoResponseDTO> obtenerEntrenamientosEntreFechas(LocalDateTime inicio, LocalDateTime fin);
}
