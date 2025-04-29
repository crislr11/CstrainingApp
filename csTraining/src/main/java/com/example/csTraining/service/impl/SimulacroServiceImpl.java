package com.example.csTraining.service.impl;

import com.example.csTraining.entity.Ejercicio;
import com.example.csTraining.entity.Simulacro;
import com.example.csTraining.repository.EjercicioRepository;
import com.example.csTraining.repository.SimulacroRepository;
import com.example.csTraining.service.SimulacroService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("simulacroService")
@RequiredArgsConstructor
public class SimulacroServiceImpl implements SimulacroService {

    @Autowired
    @Qualifier("simulacroRepository")
    private SimulacroRepository simulacroRepository;

    @Autowired
    @Qualifier("ejercicioRepository")
    private EjercicioRepository ejercicioRepository;

    // Crea un nuevo simulacro y lo guarda en la base de datos
    @Override
    public Simulacro crearSimulacro(Long userId, Simulacro simulacro) {
        if (simulacro == null) {
            throw new IllegalArgumentException("Simulacro no puede ser nulo.");
        }
        return simulacroRepository.save(simulacro);
    }

    // Obtiene todos los simulacros disponibles para el usuario
    @Override
    public List<Simulacro> obtenerSimulacrosDeUsuario(Long userId) {
        return simulacroRepository.findAll();
    }

    // Obtiene un simulacro por su ID, lanza excepción si no se encuentra
    public Simulacro obtenerSimulacroPorId(Long id) {
        return simulacroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Simulacro con ID " + id + " no encontrado."));
    }

    // Obtiene todos los ejercicios relacionados a un simulacro
    public List<Ejercicio> obtenerEjerciciosDeSimulacro(Long id) {
        Simulacro simulacro = obtenerSimulacroPorId(id);
        return simulacro.getEjercicios();
    }

    /* Modifica un simulacro existente y lo guarda en la base de datos
    public Simulacro modificarSimulacro(Long id, Simulacro simulacro) {
        Simulacro simulacroExistente = obtenerSimulacroPorId(id);
        simulacroExistente.setFecha(simulacro.getFecha());
        return simulacroRepository.save(simulacroExistente);
    }*/

    // Elimina un simulacro de la base de datos
    public void eliminarSimulacro(Long id) {
        Simulacro simulacro = obtenerSimulacroPorId(id);
        simulacroRepository.delete(simulacro);
    }

    // Añade un ejercicio a un simulacro
    public Ejercicio añadirEjercicioASimulacro(Long id, Ejercicio ejercicio) {
        if (ejercicio == null) {
            throw new IllegalArgumentException("El ejercicio no puede ser nulo.");
        }
        Simulacro simulacro = obtenerSimulacroPorId(id);
        ejercicio.setSimulacro(simulacro);
        return ejercicioRepository.save(ejercicio);
    }

    // Elimina un ejercicio de un simulacro, verificando que pertenece al simulacro especificado
    public void eliminarEjercicioDeSimulacro(Long simulacroId, Long ejercicioId) {
        Simulacro simulacro = obtenerSimulacroPorId(simulacroId);
        Optional<Ejercicio> ejercicioOpt = ejercicioRepository.findById(ejercicioId);

        if (ejercicioOpt.isPresent()) {
            Ejercicio ejercicio = ejercicioOpt.get();
            if (ejercicio.getSimulacro().equals(simulacro)) {
                ejercicioRepository.delete(ejercicio);
            } else {
                throw new IllegalArgumentException("El ejercicio no pertenece al simulacro especificado.");
            }
        } else {
            throw new EntityNotFoundException("Ejercicio con ID " + ejercicioId + " no encontrado.");
        }
    }
}
