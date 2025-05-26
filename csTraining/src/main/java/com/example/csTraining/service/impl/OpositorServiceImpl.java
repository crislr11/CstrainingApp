package com.example.csTraining.service.impl;

import com.example.csTraining.controller.DTO.MarcaOpositorDTO;
import com.example.csTraining.controller.DTO.response.EntrenamientoInscripcionResponse;
import com.example.csTraining.controller.DTO.response.EntrenamientoResponseOpositor;
import com.example.csTraining.controller.DTO.UserDTO;
import com.example.csTraining.entity.Entrenamiento;
import com.example.csTraining.entity.MarcaOpositor;
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
import java.util.stream.Collectors;

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

    @Override
    @Transactional
    public void addMarca(MarcaOpositorDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("Usuario con ID " + dto.getUserId() + " no encontrado"));
        user.getMarcasOpositor().add(new MarcaOpositor(
                dto.getEjercicioId(),
                dto.getValor(),
                dto.getFecha() != null ? dto.getFecha() : LocalDateTime.now()
        ));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void removeMarca(MarcaOpositorDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("Usuario con ID " + dto.getUserId() + " no encontrado"));
        boolean removed = user.getMarcasOpositor().removeIf(m ->
                m.getEjercicioId().equals(dto.getEjercicioId()) &&
                        m.getValor().equals(dto.getValor()) &&
                        (dto.getFecha() == null || m.getFecha().equals(dto.getFecha()))
        );
        if (!removed) {
            throw new IllegalStateException("No se encontró la marca especificada para eliminar.");
        }
        userRepository.save(user);
    }

    @Override
    public List<MarcaOpositorDTO> getMarcasPorFecha(Long userId, LocalDateTime desde, LocalDateTime hasta) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario con ID " + userId + " no encontrado"));
        return user.getMarcasOpositor().stream()
                .filter(m -> !m.getFecha().isBefore(desde) && !m.getFecha().isAfter(hasta))
                .map(m -> {
                    MarcaOpositorDTO dto = new MarcaOpositorDTO();
                    dto.setUserId(userId);
                    dto.setEjercicioId(m.getEjercicioId());
                    dto.setValor(m.getValor());
                    dto.setFecha(m.getFecha());
                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    public List<MarcaOpositorDTO> getTodasLasMarcas(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario con ID " + userId + " no encontrado"));
        return user.getMarcasOpositor().stream().map(m -> {
            MarcaOpositorDTO dto = new MarcaOpositorDTO();
            dto.setUserId(userId);
            dto.setEjercicioId(m.getEjercicioId());
            dto.setValor(m.getValor());
            dto.setFecha(m.getFecha());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<MarcaOpositorDTO> getMarcasPorEjercicio(Long userId, Long ejercicioId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario con ID " + userId + " no encontrado"));
        return user.getMarcasOpositor().stream()
                .filter(m -> m.getEjercicioId().equals(ejercicioId))
                .map(m -> {
                    MarcaOpositorDTO dto = new MarcaOpositorDTO();
                    dto.setUserId(userId);
                    dto.setEjercicioId(m.getEjercicioId());
                    dto.setValor(m.getValor());
                    dto.setFecha(m.getFecha());
                    return dto;
                }).collect(Collectors.toList());
    }


}