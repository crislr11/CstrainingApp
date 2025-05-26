package com.example.csTraining.service.impl.simulacro;


import com.example.csTraining.controller.DTO.response.EjercicioMarcaRequestDTO;
import com.example.csTraining.controller.DTO.response.EjercicioMarcaResponseDTO;
import com.example.csTraining.entity.simulacros.Ejercicio;
import com.example.csTraining.entity.simulacros.EjercicioMarca;
import com.example.csTraining.entity.simulacros.Simulacro;
import com.example.csTraining.repository.simulacros.EjercicioMarcaRepository;
import com.example.csTraining.repository.simulacros.EjercicioRepository;
import com.example.csTraining.repository.simulacros.SimulacroRepository;
import com.example.csTraining.service.simulacro.EjercicioMarcaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service("ejercicioMarcaService")
@RequiredArgsConstructor
public class EjercicioMarcaServiceImpl implements EjercicioMarcaService {

    @Autowired
    @Qualifier("ejercicioMarcaRepository")
    private EjercicioMarcaRepository ejercicioMarcaRepository;

    @Autowired
    @Qualifier("simulacroRepository")
    private SimulacroRepository simulacroRepository;

    @Autowired
    @Qualifier("ejercicioRepository")
    private EjercicioRepository ejercicioRepository;

    @Override
    public EjercicioMarcaResponseDTO guardarMarca(EjercicioMarcaRequestDTO dto) {
        validarDTO(dto);

        Simulacro simulacro = simulacroRepository.findById(dto.getSimulacroId())
                .orElseThrow(() -> new NoSuchElementException("Simulacro no encontrado con ID: " + dto.getSimulacroId()));

        Ejercicio ejercicio = ejercicioRepository.findById(dto.getEjercicioId())
                .orElseThrow(() -> new NoSuchElementException("Ejercicio no encontrado con ID: " + dto.getEjercicioId()));

        EjercicioMarca marca = convertirAEntidad(dto, simulacro, ejercicio);
        EjercicioMarca guardada = ejercicioMarcaRepository.save(marca);

        return convertirAResponseDTO(guardada);
    }

    @Override
    public List<EjercicioMarcaResponseDTO> listarMarcasPorEjercicio(Long ejercicioId) {
        Ejercicio ejercicio = ejercicioRepository.findById(ejercicioId)
                .orElseThrow(() -> new NoSuchElementException("Ejercicio no encontrado con ID: " + ejercicioId));

        return ejercicioMarcaRepository.findByEjercicio(ejercicio).stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EjercicioMarcaResponseDTO> listarTop5PorEjercicio(Long ejercicioId) {
        Ejercicio ejercicio = ejercicioRepository.findById(ejercicioId)
                .orElseThrow(() -> new NoSuchElementException("Ejercicio no encontrado con ID: " + ejercicioId));

        return ejercicioMarcaRepository.findTop5ByEjercicioOrderByMarcaDesc(ejercicio).stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EjercicioMarcaResponseDTO> listarMarcasPorSimulacro(Long simulacroId) {
        Simulacro simulacro = simulacroRepository.findById(simulacroId)
                .orElseThrow(() -> new NoSuchElementException("Simulacro no encontrado con ID: " + simulacroId));

        return ejercicioMarcaRepository.findBySimulacro(simulacro).stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EjercicioMarcaResponseDTO> obtenerRankingUsuario(Long userId, String ejercicioNombre) {
        return ejercicioMarcaRepository.findBySimulacroUserIdAndEjercicioNombreOrderByMarcaDesc(userId, ejercicioNombre).stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void eliminarMarca(Long id) {
        if (!ejercicioMarcaRepository.existsById(id)) {
            throw new NoSuchElementException("No se encontró una marca con ID: " + id);
        }
        ejercicioMarcaRepository.deleteById(id);
    }


    private EjercicioMarca convertirAEntidad(EjercicioMarcaRequestDTO dto, Simulacro simulacro, Ejercicio ejercicio) {
        return EjercicioMarca.builder()
                .marca(dto.getMarca())
                .simulacro(simulacro)
                .ejercicio(ejercicio)
                .build();
    }

    private EjercicioMarcaResponseDTO convertirAResponseDTO(EjercicioMarca em) {
        return EjercicioMarcaResponseDTO.builder()
                .id(em.getId())
                .marca(em.getMarca())
                .simulacroId(em.getSimulacro().getId())
                .ejercicioId(em.getEjercicio().getId())
                .nombre(em.getEjercicio().getNombre())
                .build();
    }



    private void validarDTO(EjercicioMarcaRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO no puede ser nulo.");
        }
        if (dto.getMarca() == null) {
            throw new IllegalArgumentException("La marca no puede ser nula.");
        }
        if (dto.getSimulacroId() == null) {
            throw new IllegalArgumentException("El ID del simulacro no puede ser nulo.");
        }
        if (dto.getEjercicioId() == null) {
            throw new IllegalArgumentException("El ID del ejercicio no puede ser nulo.");
        }
    }


}
