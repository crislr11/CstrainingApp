package com.example.csTraining.service;

import com.example.csTraining.entity.Entrenamiento;
import com.example.csTraining.entity.enums.Oposicion;
import com.example.csTraining.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EntrenamientoService {

    // Admin: Puede crear un nuevo entrenamiento
    Entrenamiento createTraining(Entrenamiento training);

    // Admin y Profesor: Pueden eliminar un entrenamiento
    void deleteTraining(Long id, User user);

    // Admin: Puede actualizar cualquier entrenamiento
    Entrenamiento updateTraining(Long id, Entrenamiento training,User user);

    // Admin: Puede obtener todas las clases
    List<Entrenamiento> getAllTrainings();

    // Profesor: Puede ver solo sus clases
    List<Entrenamiento> getTrainingsByProfessor(User professor);

    // Opositor: Puede ver solo las clases de su oposición
    List<Entrenamiento> getTrainingsByOpposition(Oposicion opposition);

    // Obtener entrenamiento por ID (para validaciones)
    Optional<Entrenamiento> getTrainingById(Long id);

    // Opositor: Ver entrenamientos futuros de su oposición
    List<Entrenamiento> getFutureTrainingsByOpposition(Oposicion oposicion, LocalDateTime fechaReferencia);

    // Profesor: Ver entrenamientos futuros a los que está asigando
    List<Entrenamiento> getFutureTrainingsByProfessor(Long professorId);

    //Admin: Ver entrenamientos entre dos fechas
    List<Entrenamiento> obtenerEntrenamientosEntreFechas(LocalDateTime inicio, LocalDateTime fin);

}
