package com.example.csTraining.service.impl;

import com.example.csTraining.controller.DTO.EntrenamientoInscripcionResponse;
import com.example.csTraining.controller.DTO.EntrenamientoResponseDTO;
import com.example.csTraining.controller.DTO.EntrenamientoResponseOpositor;
import com.example.csTraining.controller.DTO.UserDTO;
import com.example.csTraining.entity.Entrenamiento;
import com.example.csTraining.entity.User;
import com.example.csTraining.entity.enums.Role;
import com.example.csTraining.exceptions.UserNotFoundException;
import com.example.csTraining.repository.EntrenamientoRepository;
import com.example.csTraining.repository.UserRepository;
import com.example.csTraining.service.OpositorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OpositorServiceImpl implements OpositorService {

    @Autowired
    @Qualifier("entrenamientoRepository")
    private EntrenamientoRepository entrenamientoRepository;


    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;

    @Override
    @Transactional
    public EntrenamientoInscripcionResponse apuntarseEntrenamiento(Long entrenamientoId, Long userId) {
        Entrenamiento entrenamiento = entrenamientoRepository.findById(entrenamientoId)
                .orElseThrow(() -> new RuntimeException("Entrenamiento no encontrado."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario con ID " + userId + " no encontrado"));

        // Verificar que el usuario es OPOSITOR
        if (!Role.OPOSITOR.equals(user.getRole())) {
            throw new IllegalStateException("Solo los usuarios con rol OPOSITOR pueden apuntarse a entrenamientos");
        }

        // Verificar créditos disponibles
        if (user.getCreditos() <= 0) {
            throw new IllegalStateException("No tienes créditos suficientes para apuntarte a este entrenamiento");
        }

        if (entrenamiento.getAlumnos().contains(user)) {
            throw new IllegalStateException("El usuario ya está apuntado a este entrenamiento");
        }

        // Restar un crédito
        user.setCreditos(user.getCreditos() - 1);

        // Apuntar al entrenamiento
        entrenamiento.getAlumnos().add(user);

        // Guardar cambios
        userRepository.save(user);
        entrenamientoRepository.save(entrenamiento);

        return new EntrenamientoInscripcionResponse(
                "Apuntado al entrenamiento correctamente. Créditos restantes: " + user.getCreditos(),
                entrenamientoId,
                userId,
                true,
                user.getCreditos()  // Añadir créditos restantes a la respuesta
        );
    }

    @Override
    @Transactional
    public EntrenamientoInscripcionResponse desapuntarseEntrenamiento(Long entrenamientoId, Long userId) {
        Entrenamiento entrenamiento = entrenamientoRepository.findById(entrenamientoId)
                .orElseThrow(() -> new RuntimeException("Entrenamiento no encontrado."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario con ID " + userId + " no encontrado"));

        if (!entrenamiento.getAlumnos().contains(user)) {
            throw new IllegalStateException("El usuario no está apuntado a este entrenamiento");
        }

        // Devolver crédito solo si el entrenamiento está en el futuro
        boolean devolverCredito = entrenamiento.getFecha().isAfter(LocalDateTime.now());
        if (devolverCredito) {
            user.setCreditos(user.getCreditos() + 1);
            userRepository.save(user);
        }

        entrenamiento.getAlumnos().remove(user);
        entrenamientoRepository.save(entrenamiento);

        String message = devolverCredito ?
                "Desapuntado del entrenamiento correctamente. Se ha devuelto 1 crédito." :
                "Desapuntado del entrenamiento correctamente.";

        return new EntrenamientoInscripcionResponse(
                message,
                entrenamientoId,
                userId,
                false,
                user.getCreditos()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntrenamientoResponseOpositor> obtenerEntrenamientosDelOpositor(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario con ID " + userId + " no encontrado"));

        List<Entrenamiento> entrenamientos = entrenamientoRepository.findAllByAlumnosContaining(user);

        if (entrenamientos.isEmpty()) {
            throw new RuntimeException("No tienes entrenamientos apuntados");
        }

        return entrenamientos.stream()
                .map(ent -> new EntrenamientoResponseOpositor(
                        ent.getId(),
                        ent.getOposicion(),
                        ent.getProfesores().stream().map(this::mapToUserDTO).toList(),
                        ent.getFecha(),
                        ent.getLugar()
                ))
                .toList();
    }

    private UserDTO mapToUserDTO(User user) {
        return new UserDTO(user.getId(), user.getNombreUsuario(), user.getEmail(), user.getRole());
    }



}