package com.example.csTraining.service.impl;

import com.example.csTraining.controller.DTO.EntrenamientoDTO;
import com.example.csTraining.controller.DTO.EntrenamientoResponseDTO;
import com.example.csTraining.controller.DTO.UserDTO;
import com.example.csTraining.entity.Entrenamiento;
import com.example.csTraining.entity.User;
import com.example.csTraining.entity.enums.Oposicion;
import com.example.csTraining.entity.enums.Role;
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
import java.util.stream.Collectors;

@Service("entrenamientoService")
@NoArgsConstructor(force = true)
public class EntrenamientoServiceImpl implements EntrenamientoService {

    @Autowired
    @Qualifier("entrenamientoRepository")
    private final EntrenamientoRepository entrenamientoRepository;

    @Autowired
    @Qualifier("userRepository")
    private final UserRepository userRepository;

    // Métodos para convertir entre Entidades y DTOs
    private EntrenamientoResponseDTO convertToResponseDTO(Entrenamiento entrenamiento) {
        EntrenamientoResponseDTO dto = new EntrenamientoResponseDTO();
        dto.setId(entrenamiento.getId());
        dto.setOposicion(entrenamiento.getOposicion());

        // Mapeo de profesores y alumnos a UserDTO
        dto.setProfesores(entrenamiento.getProfesores().stream()
                .map(profesor -> new UserDTO(profesor.getId(), profesor.getNombreUsuario(), profesor.getEmail(), profesor.getRole()))
                .collect(Collectors.toList()));

        dto.setAlumnos(entrenamiento.getAlumnos().stream()
                .map(alumno -> new UserDTO(alumno.getId(), alumno.getNombreUsuario(), alumno.getEmail(), alumno.getRole()))
                .collect(Collectors.toList()));

        dto.setFecha(entrenamiento.getFecha());
        dto.setLugar(entrenamiento.getLugar());
        return dto;
    }

    private Entrenamiento convertToEntity(EntrenamientoDTO dto) {
        Entrenamiento entrenamiento = new Entrenamiento();
        entrenamiento.setOposicion(dto.getOposicion());
        entrenamiento.setFecha(dto.getFecha());
        entrenamiento.setLugar(dto.getLugar());

        // Convertir UserDTO a User (para profesores y alumnos)
        if (dto.getProfesores() != null) {
            List<User> profesores = dto.getProfesores().stream()
                    .map(userDTO -> userRepository.findById(userDTO.getId())
                            .orElseThrow(() -> new RuntimeException("Profesor no encontrado con ID: " + userDTO.getId())))
                    .collect(Collectors.toList());
            entrenamiento.setProfesores(profesores);
        }

        if (dto.getAlumnos() != null) {
            List<User> alumnos = dto.getAlumnos().stream()
                    .map(userDTO -> userRepository.findById(userDTO.getId())
                            .orElseThrow(() -> new RuntimeException("Alumno no encontrado con ID: " + userDTO.getId())))
                    .collect(Collectors.toList());
            entrenamiento.setAlumnos(alumnos);
        }

        return entrenamiento;
    }

    @Transactional
    @Override
    public EntrenamientoResponseDTO createTraining(EntrenamientoDTO trainingDTO) {
        Entrenamiento entrenamiento = convertToEntity(trainingDTO);
        validarEntrenamiento(entrenamiento);
        Entrenamiento savedEntrenamiento = entrenamientoRepository.save(entrenamiento);
        return convertToResponseDTO(savedEntrenamiento);
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
    public EntrenamientoResponseDTO updateTraining(Long id, EntrenamientoDTO entrenamientoDTO, User user) {
        Entrenamiento existingTraining = entrenamientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrenamiento no encontrado."));

        if (user.getRole() != Role.ADMIN && !existingTraining.getProfesores().contains(user)) {
            throw new AccessDeniedException("No tienes permisos para actualizar este entrenamiento.");
        }

        // Actualizar campos básicos
        existingTraining.setOposicion(entrenamientoDTO.getOposicion());
        existingTraining.setLugar(entrenamientoDTO.getLugar());
        existingTraining.setFecha(entrenamientoDTO.getFecha());

        // Actualizar profesores si se proporcionan
        if (entrenamientoDTO.getProfesores() != null && !entrenamientoDTO.getProfesores().isEmpty()) {
            List<User> profesores = entrenamientoDTO.getProfesores().stream()
                    .map(userDTO -> userRepository.findById(userDTO.getId())
                            .orElseThrow(() -> new RuntimeException("Profesor no encontrado con ID: " + userDTO.getId())))
                    .collect(Collectors.toList());
            existingTraining.setProfesores(profesores);
        }

        // Actualizar alumnos si se proporcionan
        if (entrenamientoDTO.getAlumnos() != null) {
            List<User> alumnos = entrenamientoDTO.getAlumnos().stream()
                    .map(userDTO -> userRepository.findById(userDTO.getId())
                            .orElseThrow(() -> new RuntimeException("Alumno no encontrado con ID: " + userDTO.getId())))
                    .collect(Collectors.toList());
            existingTraining.setAlumnos(alumnos);
        }

        validarEntrenamiento(existingTraining);
        Entrenamiento updatedEntrenamiento = entrenamientoRepository.save(existingTraining);
        return convertToResponseDTO(updatedEntrenamiento);
    }

    @Override
    public List<EntrenamientoResponseDTO> getAllTrainings() {
        List<Entrenamiento> entrenamientos = entrenamientoRepository.findAll();

        if (entrenamientos.isEmpty()) {
            throw new RuntimeException("No hay entrenamientos registrados.");
        }

        return entrenamientos.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EntrenamientoResponseDTO> getTrainingsByProfessor(Long professorId) {
        User professor = userRepository.findById(professorId)
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado con ID: " + professorId));

        validarRol(professor, Role.PROFESOR);

        List<Entrenamiento> entrenamientos = entrenamientoRepository.findByProfesores(professor);

        if (entrenamientos.isEmpty()) {
            throw new RuntimeException("No se encontraron entrenamientos asignados a este profesor.");
        }

        return entrenamientos.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EntrenamientoResponseDTO> getTrainingsByOpposition(Oposicion opposition) {
        List<Entrenamiento> entrenamientos = entrenamientoRepository.findByOposicion(opposition);

        if (entrenamientos == null || entrenamientos.isEmpty()) {
            throw new RuntimeException("No hay entrenamientos disponibles para la oposición: " + opposition);
        }

        return entrenamientos.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EntrenamientoResponseDTO getTrainingById(Long id) {
        Entrenamiento entrenamiento = entrenamientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No hay entrenamientos disponibles con el ID: " + id));
        return convertToResponseDTO(entrenamiento);
    }

    @Override
    public List<EntrenamientoResponseDTO> getFutureTrainingsByOpposition(Oposicion oposicion, LocalDateTime fechaReferencia) {
        List<Entrenamiento> entrenamientos = entrenamientoRepository.findByOposicionAndFechaAfter(oposicion, fechaReferencia);

        if (entrenamientos == null || entrenamientos.isEmpty()) {
            throw new RuntimeException("No hay entrenamientos futuros disponibles para la oposición: " + oposicion);
        }

        return entrenamientos.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EntrenamientoResponseDTO> getFutureTrainingsByProfessor(Long professorId) {
        User professor = userRepository.findById(professorId)
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado con ID: " + professorId));

        if (professor.getRole() != Role.PROFESOR) {
            throw new RuntimeException("Acceso denegado: Solo los profesores pueden ver sus entrenamientos.");
        }

        List<Entrenamiento> entrenamientos = entrenamientoRepository.findByProfesoresAndFechaAfter(professor, LocalDateTime.now());

        if (entrenamientos.isEmpty()) {
            throw new RuntimeException("No se encontraron entrenamientos futuros asignados a este profesor.");
        }

        return entrenamientos.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EntrenamientoResponseDTO> obtenerEntrenamientosEntreFechas(LocalDateTime inicio, LocalDateTime fin) {
        if (inicio == null || fin == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser nulas.");
        }

        if (inicio.isAfter(fin)) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior a la fecha de fin.");
        }

        List<Entrenamiento> entrenamientos = entrenamientoRepository.findByFechaBetween(inicio, fin);
        return entrenamientos.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // Métodos privados de validación
    private void validarRol(User user, Role expectedRole) {
        if (user.getRole() != expectedRole) {
            throw new AccessDeniedException("Acceso denegado: se requiere el rol " + expectedRole);
        }
    }

    private void validarEntrenamiento(Entrenamiento training) {
        if (training.getProfesores() == null || training.getProfesores().isEmpty()) {
            throw new RuntimeException("Un entrenamiento debe tener al menos un profesor.");
        }
    }
}
