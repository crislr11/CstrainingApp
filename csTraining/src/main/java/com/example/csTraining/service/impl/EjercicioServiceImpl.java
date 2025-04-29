package com.example.csTraining.service.impl;

import com.example.csTraining.entity.Ejercicio;
import com.example.csTraining.repository.EjercicioRepository;
import com.example.csTraining.service.EjercicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service("ejercicioService")
@RequiredArgsConstructor
public class EjercicioServiceImpl implements EjercicioService {

    private final EjercicioRepository ejercicioRepository;

    @Override
    public Ejercicio crearEjercicio(Ejercicio ejercicio) {
        return ejercicioRepository.save(ejercicio);
    }

    @Override
    public List<Ejercicio> obtenerTodosEjercicios() {
        return ejercicioRepository.findAll();
    }

    @Override
    public List<Ejercicio> obtenerEjerciciosPorSimulacro(Long idSimulacro) {
        return ejercicioRepository.findBySimulacroId(idSimulacro);
    }

    @Override
    @Transactional
    public Ejercicio modificarNombreEjercicio(Long id, String nuevoNombre) {
        Ejercicio ejercicio = ejercicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ejercicio no encontrado"));

        ejercicio.setNombreEjercicio(nuevoNombre);
        return ejercicioRepository.save(ejercicio);
    }

    @Override
    @Transactional
    public Ejercicio modificarMarcaEjercicio(Long id, double nuevaMarca) {
        Ejercicio ejercicio = ejercicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ejercicio no encontrado"));

        ejercicio.setMarca(nuevaMarca);
        return ejercicioRepository.save(ejercicio);
    }

    @Override
    public void eliminarEjercicio(Long id) {
        ejercicioRepository.deleteById(id);
    }
}
