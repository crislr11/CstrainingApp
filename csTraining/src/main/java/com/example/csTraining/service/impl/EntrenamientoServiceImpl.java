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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
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

    @Transactional
    @Override
    public Entrenamiento createTraining(Entrenamiento training) {
        // Validar que todos los profesores tienen el rol correcto
        if (training.getProfesores() != null) {
            for (User profesor : training.getProfesores()) {
                if (profesor.getRole() == null || profesor.getRole() != Role.PROFESOR) {
                    throw new RuntimeException("Todos los profesores deben tener el rol de PROFESOR.");
                }
            }
        }

        // Validar que todos los alumnos tienen el rol correcto
        if (training.getAlumnos() != null) {
            for (User alumno : training.getAlumnos()) {
                if (alumno.getRole() == null || alumno.getRole() != Role.OPOSITOR) {
                    throw new RuntimeException("Todos los alumnos deben tener el rol de OPOSITOR.");
                }
            }
        }

        // Validación: Un entrenamiento no puede tener más de 2 profesores
        if (training.getProfesores() != null && training.getProfesores().size() > 2) {
            throw new RuntimeException("Un entrenamiento no puede tener más de 2 profesores.");
        }

        // Validar que la fecha no sea una fecha pasada
        if (training.getFecha() != null && training.getFecha().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("La fecha del entrenamiento no puede ser anterior a la fecha actual.");
        }

        // Guardar el entrenamiento en la base de datos
        return entrenamientoRepository.save(training);
    }

    @Transactional
    @Override
    public void deleteTraining(Long id, User user) {
        // Obtener el entrenamiento por su ID
        Optional<Entrenamiento> optionalEntrenamiento = entrenamientoRepository.findById(id);

        if (!optionalEntrenamiento.isPresent()) {
            throw new NoSuchElementException("Entrenamiento no encontrado.");
        }

        Entrenamiento entrenamiento = optionalEntrenamiento.get();

        // Verificar si el usuario es admin o es uno de los profesores asignados
        if (user.getRole() == Role.ADMIN || entrenamiento.getProfesores().contains(user)) {
            // Eliminar el entrenamiento
            entrenamientoRepository.delete(entrenamiento);
        } else {
            throw new RuntimeException("No tienes permisos para eliminar este entrenamiento.");
        }
    }

    @Transactional
    @Override
    public Entrenamiento updateTraining(Long id, Entrenamiento entrenamiento,User user) {
        Optional<Entrenamiento> optionalEntrenamiento = entrenamientoRepository.findById(id);

        if (!optionalEntrenamiento.isPresent()) {
            throw new NoSuchElementException("Entrenamiento no encontrado.");
        }

        Entrenamiento training = optionalEntrenamiento.get();
        if (user.getRole() == Role.ADMIN || training.getProfesores().contains(user)) {
            training.setFecha(entrenamiento.getFecha());
            training.setLugar(entrenamiento.getLugar());
        } else {
            throw new RuntimeException("No tienes permisos para eliminar este entrenamiento.");
        }

        return entrenamientoRepository.save(training);
    }

    @Override
    public List<Entrenamiento> getAllTrainings() {
        List<Entrenamiento> entrenamientos = entrenamientoRepository.findAll();

        if (entrenamientos.isEmpty()) {
            throw new RuntimeException("No hay entrenamientos registrados.");
        }

        return entrenamientos;
    }

    @Override
    public List<Entrenamiento> getTrainingsByProfessor(User professor) {
        if (professor.getRole() != Role.PROFESOR) {
            throw new AccessDeniedException("Acceso denegado: Solo los profesores pueden ver sus entrenamientos.");
        }

        List<Entrenamiento> entrenamientos = entrenamientoRepository.findByProfesores(professor);

        if (entrenamientos.isEmpty()) {
            throw new RuntimeException("No se encontraron entrenamientos asignados a este profesor.");
        }

        return entrenamientos;
    }


    @Override
    public List<Entrenamiento> getTrainingsByOpposition(Oposicion opposition) {
        List<Entrenamiento> entrenamientos = entrenamientoRepository.findByOposicion(opposition);
        if (entrenamientos == null || entrenamientos.isEmpty()) {
            throw new RuntimeException("No hay entrenamientos disponibles para la oposición: " + opposition);
        }
        return entrenamientos;
    }

    @Override
    public Optional<Entrenamiento> getTrainingById(Long id) {
        Optional<Entrenamiento> optionalTraining = entrenamientoRepository.findById(id);
        if (!optionalTraining.isPresent()) {
            throw new RuntimeException("No hay entrenamientos disponibles con el ID: " + id);
        }
        return optionalTraining;
    }


}

