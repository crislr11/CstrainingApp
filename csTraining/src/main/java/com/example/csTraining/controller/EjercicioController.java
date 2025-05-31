package com.example.csTraining.controller;

import com.example.csTraining.controller.DTO.response.EjercicioRequestDTO;
import com.example.csTraining.controller.DTO.response.EjercicioResponseDTO;
import com.example.csTraining.service.simulacro.EjercicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/ejercicio")
@RequiredArgsConstructor
public class EjercicioController {

    private final EjercicioService ejercicioService;

    // Obtener ejercicio por nombre - Solo para OPOSITORES
    @PreAuthorize("hasRole('OPOSITOR')")
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<?> getEjercicioPorNombre(@PathVariable String nombre) {
        try {
            EjercicioResponseDTO ejercicio = ejercicioService.buscarEjercicioPorNombre(nombre);
            return ResponseEntity.ok(ejercicio);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Crear un nuevo ejercicio - Solo para PROFESORES
    @PreAuthorize("hasRole('PROFESOR')")
    @PostMapping
    public ResponseEntity<?> crearEjercicio(@RequestBody EjercicioRequestDTO ejercicioDTO) {
        try {
            EjercicioResponseDTO savedEjercicio = ejercicioService.guardarEjercicio(ejercicioDTO);
            return new ResponseEntity<>(savedEjercicio, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Eliminar un ejercicio - Solo para PROFESORES
    @PreAuthorize("hasRole('PROFESOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEjercicio(@PathVariable Long id) {
        try {
            ejercicioService.eliminarEjercicio(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Obtener todos los ejercicios
    @PreAuthorize("hasAnyRole('OPOSITOR','PROFESOR')")
    @GetMapping
    public ResponseEntity<?> listarEjercicios() {
        try {
            List<EjercicioResponseDTO> ejercicios = ejercicioService.listarTodosEjercicios();
            return ResponseEntity.ok(ejercicios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener los ejercicios: " + e.getMessage());
        }
    }
}
