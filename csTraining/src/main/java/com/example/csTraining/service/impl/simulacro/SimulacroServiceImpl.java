package com.example.csTraining.service.impl.simulacro;


import com.example.csTraining.controller.DTO.response.EjercicioMarcaRequestDTO;
import com.example.csTraining.controller.DTO.response.EjercicioMarcaResponseDTO;
import com.example.csTraining.controller.DTO.response.SimulacroRequestDTO;
import com.example.csTraining.controller.DTO.response.SimulacroResponseDTO;
import com.example.csTraining.entity.User;
import com.example.csTraining.entity.simulacros.Ejercicio;
import com.example.csTraining.entity.simulacros.EjercicioMarca;
import com.example.csTraining.entity.simulacros.Simulacro;
import com.example.csTraining.exceptions.ResourceNotFoundException;
import com.example.csTraining.repository.UserRepository;
import com.example.csTraining.repository.simulacros.EjercicioMarcaRepository;
import com.example.csTraining.repository.simulacros.EjercicioRepository;
import com.example.csTraining.repository.simulacros.SimulacroRepository;
import com.example.csTraining.service.simulacro.SimulacroService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service("simulacroService")
@RequiredArgsConstructor
public class SimulacroServiceImpl implements SimulacroService {

    @Autowired
    @Qualifier("simulacroRepository")
    private SimulacroRepository simulacroRepository;

    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;

    @Autowired
    @Qualifier("ejercicioRepository")
    private EjercicioRepository ejercicioRepository;

    @Autowired
    @Qualifier("ejercicioMarcaRepository")
    private EjercicioMarcaRepository ejercicioMarcaRepository;

    @Override
    public List<SimulacroResponseDTO> getSimulacrosPorUsuario(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return simulacroRepository.findByUser(user)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SimulacroResponseDTO saveSimulacro(SimulacroRequestDTO dto) {
        // Convertir el DTO a entidad
        Simulacro simulacro = convertToEntity(dto);
        // Guardar el simulacro
        Simulacro saved = simulacroRepository.save(simulacro);
        // Devolver el DTO de la respuesta
        return convertToResponseDTO(saved);
    }

    @Override
    public List<SimulacroResponseDTO> getSimulacrosPorFecha(LocalDate startDate, LocalDate endDate) {
        return simulacroRepository.findByFechaBetween(startDate, endDate)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void eliminarSimulacro(Long id) {
        simulacroRepository.deleteById(id);
    }

    @Override
    public SimulacroResponseDTO asignarSimulacroAUsuario(Long simulacroId, Long userId) {
        Simulacro simulacro = simulacroRepository.findById(simulacroId)
                .orElseThrow(() -> new ResourceNotFoundException("Simulacro no encontrado"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        simulacro.setUser(user);
        Simulacro updated = simulacroRepository.save(simulacro);
        return convertToResponseDTO(updated);
    }

    @Override
    public SimulacroResponseDTO agregarEjercicioASimulacro(Long simulacroId, EjercicioMarcaRequestDTO dto) {
        Simulacro simulacro = simulacroRepository.findById(simulacroId)
                .orElseThrow(() -> new ResourceNotFoundException("Simulacro no encontrado con ID: " + simulacroId));

        Ejercicio ejercicio = ejercicioRepository.findById(dto.getEjercicioId())
                .orElseThrow(() -> new ResourceNotFoundException("Ejercicio no encontrado con ID: " + dto.getEjercicioId()));

        // Ahora incluimos el nombre en la creación del EjercicioMarca
        EjercicioMarca ejercicioMarca = EjercicioMarca.builder()
                .marca(dto.getMarca())
                .simulacro(simulacro)
                .ejercicio(ejercicio)
                .nombre(dto.getNombre())
                .build();

        ejercicioMarcaRepository.save(ejercicioMarca);
        simulacro.getEjercicios().add(ejercicioMarca);
        Simulacro updated = simulacroRepository.save(simulacro);

        return convertToResponseDTO(updated);
    }


    // --- MÉTODOS DE CONVERSIÓN ---

    private SimulacroResponseDTO convertToResponseDTO(Simulacro simulacro) {
        return SimulacroResponseDTO.builder()
                .id(simulacro.getId())
                .titulo(simulacro.getTitulo())
                .fecha(simulacro.getFecha())
                .userId(simulacro.getUser() != null ? simulacro.getUser().getId() : null)
                .ejercicios(simulacro.getEjercicios()
                        .stream()
                        .map(this::convertToEjercicioMarcaDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    private EjercicioMarcaResponseDTO convertToEjercicioMarcaDTO(EjercicioMarca marca) {
        return EjercicioMarcaResponseDTO.builder()
                .id(marca.getId())
                .marca(marca.getMarca())
                .simulacroId(marca.getSimulacro().getId())
                .ejercicioId(marca.getEjercicio().getId())
                .nombre(marca.getNombre())  // Asegúrate de incluir el nombre aquí
                .build();
    }

    private Simulacro convertToEntity(SimulacroRequestDTO dto) {
        // Si userId es nulo, el simulacro no tendrá un usuario asignado
        User user = null;
        if (dto.getUserId() != null) {
            user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + dto.getUserId()));
        }

        // Convertir los EjercicioMarcaRequestDTO a EjercicioMarca (entidad)
        List<EjercicioMarca> ejercicios = dto.getEjercicios().stream()
                .map(ejercicioMarcaRequestDTO -> {
                    // Recuperar el Ejercicio correspondiente desde la base de datos
                    Ejercicio ejercicio = ejercicioRepository.findById(ejercicioMarcaRequestDTO.getEjercicioId())
                            .orElseThrow(() -> new ResourceNotFoundException("Ejercicio no encontrado con ID: " + ejercicioMarcaRequestDTO.getEjercicioId()));

                    // Crear la entidad EjercicioMarca y asignar el simulacro y ejercicio correspondientes
                    return EjercicioMarca.builder()
                            .marca(ejercicioMarcaRequestDTO.getMarca())
                            .simulacro(null) // Este campo será asignado después al simulacro
                            .ejercicio(ejercicio)
                            .build();
                })
                .collect(Collectors.toList());

        // Convertir el DTO en la entidad Simulacro
        Simulacro simulacro = Simulacro.builder()
                .titulo(dto.getTitulo())
                .fecha(dto.getFecha())
                .user(user)  // Aquí asignamos el user (si existe)
                .ejercicios(ejercicios)
                .build();

        // Asignar el simulacro a cada EjercicioMarca (relación bidireccional)
        for (EjercicioMarca ejercicioMarca : ejercicios) {
            ejercicioMarca.setSimulacro(simulacro);
        }

        return simulacro;
    }
}

