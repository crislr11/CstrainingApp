package com.example.csTraining.service.impl.simulacro;


import com.example.csTraining.controller.DTO.EjercicioRequestDTO;
import com.example.csTraining.controller.DTO.EjercicioResponseDTO;
import com.example.csTraining.entity.simulacros.Ejercicio;
import com.example.csTraining.repository.simulacros.EjercicioRepository;
import com.example.csTraining.service.simulacro.EjercicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service("ejercicioService")
@RequiredArgsConstructor
public class EjercicioServiceImpl implements EjercicioService {

    @Autowired
    @Qualifier("ejercicioRepository")
    private final EjercicioRepository ejercicioRepository;

    @Override
    public Ejercicio getEjercicioPorNombre(String nombre) {
        validarNombre(nombre);
        Ejercicio ejercicio = ejercicioRepository.findByNombre(nombre);
        if (ejercicio == null) {
            throw new NoSuchElementException("No se encontró un ejercicio con el nombre: " + nombre);
        }
        return ejercicio;
    }

    @Override
    public Ejercicio saveEjercicio(Ejercicio ejercicio) {
        validarEjercicio(ejercicio);
        return ejercicioRepository.save(ejercicio);
    }

    @Override
    public void eliminarEjercicio(Long id) {
        validarId(id);
        if (!ejercicioRepository.existsById(id)) {
            throw new NoSuchElementException("No se encontró un ejercicio con ID: " + id);
        }
        ejercicioRepository.deleteById(id);
    }

    @Override
    public List<Ejercicio> getAllEjercicios() {
        return ejercicioRepository.findAll();
    }

    // Nuevos métodos con DTOs
    @Override
    public EjercicioResponseDTO buscarEjercicioPorNombre(String nombre) {
        Ejercicio ejercicio = getEjercicioPorNombre(nombre);
        return convertirAResponseDTO(ejercicio);
    }

    @Override
    public EjercicioResponseDTO guardarEjercicio(EjercicioRequestDTO ejercicioDTO) {
        validarEjercicioDTO(ejercicioDTO);
        Ejercicio ejercicio = convertirAEntidad(ejercicioDTO);
        Ejercicio ejercicioGuardado = ejercicioRepository.save(ejercicio);
        return convertirAResponseDTO(ejercicioGuardado);
    }

    @Override
    public List<EjercicioResponseDTO> listarTodosEjercicios() {
        return ejercicioRepository.findAll().stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // Métodos de conversión
    private EjercicioResponseDTO convertirAResponseDTO(Ejercicio ejercicio) {
        return EjercicioResponseDTO.builder()
                .id(ejercicio.getId())
                .nombre(ejercicio.getNombre())
                .descripcion(ejercicio.getDescripcion())
                .build();
    }

    private Ejercicio convertirAEntidad(EjercicioRequestDTO ejercicioDTO) {
        return Ejercicio.builder()
                .nombre(ejercicioDTO.getNombre())
                .descripcion(ejercicioDTO.getDescripcion())
                .build();
    }

    // Métodos de validación
    private void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del ejercicio no puede ser nulo o vacío.");
        }
    }

    private void validarEjercicio(Ejercicio ejercicio) {
        if (ejercicio == null || ejercicio.getNombre() == null || ejercicio.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El ejercicio o su nombre no pueden ser nulos o vacíos.");
        }
    }

    private void validarId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo.");
        }
    }

    private void validarEjercicioDTO(EjercicioRequestDTO ejercicioDTO) {
        if (ejercicioDTO == null || ejercicioDTO.getNombre() == null || ejercicioDTO.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El DTO del ejercicio o su nombre no pueden ser nulos o vacíos.");
        }
    }
}