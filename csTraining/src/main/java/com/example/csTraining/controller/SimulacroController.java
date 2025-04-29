package com.example.csTraining.controller;

import com.example.csTraining.entity.Ejercicio;
import com.example.csTraining.entity.Simulacro;
import com.example.csTraining.service.SimulacroService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/simulacro")
@RequiredArgsConstructor
public class SimulacroController {

    @Autowired
    @Qualifier("simulacroService")
    private SimulacroService simulacroService;

    // Crear un nuevo simulacro
    @PreAuthorize("hasRole('PROFESOR')")
    @PostMapping("/crear")
    public ResponseEntity<Simulacro> crearSimulacro(@RequestBody Simulacro simulacro) {
        try {
            Simulacro nuevoSimulacro = simulacroService.crearSimulacro(simulacro.getUser().getId(), simulacro);
            return new ResponseEntity<>(nuevoSimulacro, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Obtener todos los simulacros de un usuario
    @PreAuthorize("hasRole('PROFESOR')")
    @GetMapping("/todos")
    public ResponseEntity<List<Simulacro>> obtenerSimulacrosDeUsuario(@RequestParam Long userId) {
        List<Simulacro> simulacros = simulacroService.obtenerSimulacrosDeUsuario(userId);
        return new ResponseEntity<>(simulacros, HttpStatus.OK);
    }

    // Obtener un simulacro por ID
    @PreAuthorize("hasRole('PROFESOR')")
    @GetMapping("/{id}")
    public ResponseEntity<Simulacro> obtenerSimulacroPorId(@PathVariable Long id) {
        try {
            Simulacro simulacro = simulacroService.obtenerSimulacroPorId(id);
            return new ResponseEntity<>(simulacro, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar un simulacro
    @PreAuthorize("hasRole('PROFESOR')")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarSimulacro(@PathVariable Long id) {
        try {
            simulacroService.eliminarSimulacro(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Obtener todos los ejercicios de un simulacro
    @PreAuthorize("hasRole('PROFESOR')")
    @GetMapping("/ejercicios/{id}")
    public ResponseEntity<List<Ejercicio>> obtenerEjerciciosDeSimulacro(@PathVariable Long id) {
        try {
            List<Ejercicio> ejercicios = simulacroService.obtenerEjerciciosDeSimulacro(id);
            return new ResponseEntity<>(ejercicios, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Añadir un ejercicio a un simulacro
    @PreAuthorize("hasRole('PROFESOR')")
    @PostMapping("/ejercicio/{id}")
    public ResponseEntity<Ejercicio> añadirEjercicioASimulacro(@PathVariable Long id, @RequestBody Ejercicio ejercicio) {
        try {
            Ejercicio ejercicioAñadido = simulacroService.añadirEjercicioASimulacro(id, ejercicio);
            return new ResponseEntity<>(ejercicioAñadido, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Eliminar un ejercicio de un simulacro
    @PreAuthorize("hasRole('PROFESOR')")
    @DeleteMapping("/ejercicio/eliminar/{simulacroId}/{ejercicioId}")
    public ResponseEntity<Void> eliminarEjercicioDeSimulacro(@PathVariable Long simulacroId, @PathVariable Long ejercicioId) {
        try {
            simulacroService.eliminarEjercicioDeSimulacro(simulacroId, ejercicioId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
