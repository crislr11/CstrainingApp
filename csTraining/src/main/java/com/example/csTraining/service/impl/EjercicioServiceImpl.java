package com.example.csTraining.service.impl;

import com.example.csTraining.service.EjercicioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ejercicioService")
@RequiredArgsConstructor
public class EjercicioServiceImpl implements EjercicioService {

    private final EjercicioRepository ejercicioRepository;
    private final SimulacroRepository simulacroRepository;

    @Override
    public Ejercicio añadirEjercicioASimulacro(Long simulacroId, Ejercicio ejercicio) {
        Simulacro simulacro = simulacroRepository.findById(simulacroId)
                .orElseThrow(() -> new EntityNotFoundException("Simulacro con ID " + simulacroId + " no encontrado."));

        ejercicio.setSimulacro(simulacro);
        return ejercicioRepository.save(ejercicio);
    }

    @Override
    public Ejercicio editarEjercicio(Long id, Ejercicio ejercicio) {
        Ejercicio ejercicioExistente = ejercicioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ejercicio con ID " + id + " no encontrado."));

        ejercicioExistente.setNombreEjercicio(ejercicio.getNombreEjercicio());
        ejercicioExistente.setMarca(ejercicio.getMarca());
        return ejercicioRepository.save(ejercicioExistente);
    }

    @Override
    public void eliminarEjercicio(Long id) {
        Ejercicio ejercicio = ejercicioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ejercicio con ID " + id + " no encontrado."));
        ejercicioRepository.delete(ejercicio);
    }

    @Override
    public List<Ejercicio> obtenerEjerciciosPorSimulacro(Long simulacroId) {
        Simulacro simulacro = simulacroRepository.findById(simulacroId)
                .orElseThrow(() -> new EntityNotFoundException("Simulacro con ID " + simulacroId + " no encontrado."));
        return ejercicioRepository.findBySimulacroId(simulacroId);
    }
}
