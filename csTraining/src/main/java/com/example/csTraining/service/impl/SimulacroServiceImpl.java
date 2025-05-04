package com.example.csTraining.service.impl;

import com.example.csTraining.entity.User;
import com.example.csTraining.repository.UserRepository;
import com.example.csTraining.service.SimulacroService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("simulacroService")
@RequiredArgsConstructor
public class SimulacroServiceImpl implements SimulacroService {

    private final SimulacroRepository simulacroRepository;
    private final UserRepository userRepository;

    @Override
    public Simulacro crearSimulacro(Long userId, Simulacro simulacro) {
        if (simulacro == null) {
            throw new IllegalArgumentException("Simulacro no puede ser nulo.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario con ID " + userId + " no encontrado."));

        simulacro.setUser(user);
        return simulacroRepository.save(simulacro);
    }

    @Override
    public List<Simulacro> obtenerSimulacrosDeUsuario(Long userId) {
        return simulacroRepository.findByUserId(userId);
    }

    @Override
    public void eliminarSimulacro(Long id) {
        Simulacro simulacro = simulacroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Simulacro con ID " + id + " no encontrado."));
        simulacroRepository.delete(simulacro);
    }

    @Override
    public List<Ejercicio> obtenerEjerciciosDeSimulacro(Long simulacroId) {
        Simulacro simulacro = simulacroRepository.findById(simulacroId)
                .orElseThrow(() -> new EntityNotFoundException("Simulacro con ID " + simulacroId + " no encontrado."));
        return simulacro.getEjercicios();
    }

    @Override
    public Simulacro obtenerSimulacroPorId(Long id) {
        Optional<Simulacro> simulacro = simulacroRepository.findById(id);
        if (!simulacro.isPresent()) {
            throw new EntityNotFoundException("Simulacto con ID " + id + " no encontrado.");
        }
        return simulacro.get();
    }
}
