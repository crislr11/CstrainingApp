package com.example.csTraining.service.impl;

import com.example.csTraining.entity.Entrenamiento;
import com.example.csTraining.entity.Oposicion;
import com.example.csTraining.entity.Role;
import com.example.csTraining.entity.User;
import com.example.csTraining.exceptions.EntrenamientoNotFoundException;
import com.example.csTraining.repository.EntrenamientoRepository;
import com.example.csTraining.repository.UserRepository;

import com.example.csTraining.service.EntrenamientoService;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service("entrenamientoService")
@NoArgsConstructor(force = true)
public class EntrenamientoServiceImpl implements EntrenamientoService {

    @Autowired
    @Qualifier("entrenamientoRepository")
    private final EntrenamientoRepository entrenamientoRepository;

    @Autowired
    @Qualifier("userRepository")
    private final UserRepository userRepository;


    @Override
    @Transactional
    public Entrenamiento createTraining(Entrenamiento training) {
        if (training == null) {
            throw new IllegalArgumentException("El entrenamiento no puede ser nulo");
        }
        try {
            return entrenamientoRepository.save(training);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el entrenamiento: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void deleteTraining(Long id, User user) {

        // Verificar si el usuario tiene permisos para eliminar la clase
        if (user == null) {
            throw new SecurityException("Usuario no proporcionado.");
        }

        boolean isProfessor = user.getRole() == Role.PROFESOR;

        if (user.getRole() != Role.ADMIN && !isProfessor) {
            throw new SecurityException("No tienes permisos para eliminar esta clase.");
        }


        Entrenamiento entrenamiento = entrenamientoRepository.findById(id)
                .orElseThrow(() -> new EntrenamientoNotFoundException("Entrenamiento no encontrado con ID: " + id));

        // Si el usuario no es admin, verificamos si el usuario está asignado a la clase como profesor
        if (user.getRole() != Role.ADMIN) {
            boolean esProfesorDeLaClase = entrenamiento.getProfesores().stream()
                    .anyMatch(profesor -> profesor.getId().equals(user.getId()));

            if (!esProfesorDeLaClase) {
                throw new SecurityException("No tienes permisos para eliminar esta clase.");
            }
        }

        // Eliminar el entrenamiento
        try {
            entrenamientoRepository.delete(entrenamiento);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el entrenamiento: " + e.getMessage(), e);
        }
    }


    @Override
    @Transactional
    public Entrenamiento updateTraining(Long id, Entrenamiento training) {
        // Verificamos que el entrenamiento exista antes de actualizarlo
        Entrenamiento existingTraining = entrenamientoRepository.findById(id)
                .orElseThrow(() -> new EntrenamientoNotFoundException("Entrenamiento no encontrado con ID: " + id));
        existingTraining.setOposicion(training.getOposicion());
        existingTraining.setProfesores(training.getProfesores());
        existingTraining.setFecha(training.getFecha());
        existingTraining.setLugar(training.getLugar());

        try {
            return entrenamientoRepository.save(existingTraining);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el entrenamiento: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Entrenamiento> getAllTrainings() {
        try {
            return entrenamientoRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener todas las clases de entrenamiento: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Entrenamiento> getTrainingsByProfessor(User professor) {
        if (professor == null || professor.getRole() != Role.PROFESOR) {
            throw new RuntimeException("El usuario no es un profesor o no está autenticado correctamente.");
        }
        try {
            List<Entrenamiento> clases = entrenamientoRepository.findByProfesores(professor);

            if (clases.isEmpty()) {
                throw new RuntimeException("No tienes clases asignadas.");
            }
            return clases;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las clases del profesor: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Entrenamiento> getTrainingsByOpposition(Oposicion oposicion) {
        // Verificamos si la oposición está presente
        if (oposicion == null) {
            throw new IllegalArgumentException("No se ha asignado ninguna oposicion");
        }

        try {
            return entrenamientoRepository.findByOposicion(oposicion);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las clases para la oposición: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Entrenamiento> getTrainingById(Long id) {
        // Buscamos el entrenamiento por su ID
        try {
            return entrenamientoRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener el entrenamiento con ID " + id + ": " + e.getMessage(), e);
        }
    }
}

