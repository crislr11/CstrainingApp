package com.example.csTraining.service.impl;

import com.example.csTraining.entity.Entrenamiento;
import com.example.csTraining.entity.Oposicion;
import com.example.csTraining.entity.Role;
import com.example.csTraining.entity.User;
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
        validarEntrenamiento(training);
        return entrenamientoRepository.save(training);
    }

    @Transactional
    @Override
    public void deleteTraining(Long id, User user) {
        Entrenamiento entrenamiento = entrenamientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrenamiento no encontrado."));

        if (user.getRole() != Role.ADMIN && !entrenamiento.getProfesores().contains(user)) {
            throw new AccessDeniedException("No tienes permisos para eliminar este entrenamiento.");
        }

        entrenamientoRepository.delete(entrenamiento);
    }

    @Transactional
    @Override
    public Entrenamiento updateTraining(Long id, Entrenamiento entrenamiento, User user) {
        // Buscar el entrenamiento existente
        Entrenamiento training = entrenamientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrenamiento no encontrado."));
        System.out.println(entrenamiento);
        if (user.getRole() != Role.ADMIN && !training.getProfesores().contains(user)) {
            throw new AccessDeniedException("No tienes permisos para actualizar este entrenamiento.");
        }
        training.setOposicion(entrenamiento.getOposicion());
        training.setLugar(entrenamiento.getLugar());
        training.setFecha(entrenamiento.getFecha());

        if (entrenamiento.getProfesores() != null && !entrenamiento.getProfesores().isEmpty()) {
            training.setProfesores(entrenamiento.getProfesores());
        }

        if (entrenamiento.getAlumnos() != null) {
            training.setAlumnos(entrenamiento.getAlumnos());
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
        validarRol(professor, Role.PROFESOR);

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
        return entrenamientoRepository.findById(id)
                .or(() -> {
                    throw new RuntimeException("No hay entrenamientos disponibles con el ID: " + id);
                });
    }

    @Override
    public List<Entrenamiento> getFutureTrainingsByOpposition(Oposicion oposicion, LocalDateTime fechaReferencia) {
        List<Entrenamiento> entrenamientos = entrenamientoRepository.findByOposicionAndFechaAfter(oposicion, fechaReferencia);

        if (entrenamientos == null || entrenamientos.isEmpty()) {
            throw new RuntimeException("No hay entrenamientos futuros disponibles para la oposición: " + oposicion);
        }

        return entrenamientos;
    }

    @Override
    public List<Entrenamiento> getFutureTrainingsByProfessor(User professor) {
        if (professor.getRole() != Role.PROFESOR) {
            throw new RuntimeException("Acceso denegado: Solo los profesores pueden ver sus entrenamientos.");
        }

        List<Entrenamiento> entrenamientos = entrenamientoRepository.findByProfesoresAndFechaAfter(professor, LocalDateTime.now());

        if (entrenamientos.isEmpty()) {
            throw new RuntimeException("No se encontraron entrenamientos futuros asignados a este profesor.");
        }

        return entrenamientos;
    }

    @Override
    public List<Entrenamiento> obtenerEntrenamientosEntreFechas(LocalDateTime inicio, LocalDateTime fin) {
        if (inicio == null || fin == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser nulas.");
        }

        if (inicio.isAfter(fin)) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior a la fecha de fin.");
        }

        return entrenamientoRepository.findByFechaBetween(inicio, fin);
    }


    // MÉTODOS PRIVADOS

    private void validarRol(User user, Role expectedRole) {
        if (user.getRole() != expectedRole) {
            throw new AccessDeniedException("Acceso denegado: se requiere el rol " + expectedRole);
        }
    }

    private void validarEntrenamiento(Entrenamiento training) {
        if (training.getProfesores() != null) {
            for (User profesor : training.getProfesores()) {
                if (profesor.getRole() != Role.PROFESOR) {
                    throw new RuntimeException("Todos los profesores deben tener el rol de PROFESOR.");
                }
            }

            if (training.getProfesores().size() > 2) {
                throw new RuntimeException("Un entrenamiento no puede tener más de 2 profesores.");
            }
        }

        if (training.getAlumnos() != null) {
            for (User alumno : training.getAlumnos()) {
                if (alumno.getRole() != Role.OPOSITOR) {
                    throw new RuntimeException("Todos los alumnos deben tener el rol de OPOSITOR.");
                }
            }
        }

        if (training.getFecha() != null && training.getFecha().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("La fecha del entrenamiento no puede ser anterior a la actual.");
        }
    }

}
