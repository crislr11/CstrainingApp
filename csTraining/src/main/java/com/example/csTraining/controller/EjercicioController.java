package com.example.csTraining.controller;

import com.example.csTraining.entity.Ejercicio;
import com.example.csTraining.service.EjercicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ejercicios")
@RequiredArgsConstructor
public class EjercicioController {

    @Autowired
    @Qualifier("ejercicioService")
    private EjercicioService ejercicioService;

    // Crear un nuevo ejercicio
    @PostMapping
    public ResponseEntity<Ejercicio> crearEjercicio(@RequestBody Ejercicio ejercicio) {
        Ejercicio nuevoEjercicio = ejercicioService.crearEjercicio(ejercicio);
        return ResponseEntity.ok(nuevoEjercicio);
    }

    // Obtener todos los ejercicios
    @GetMapping
    public ResponseEntity<List<Ejercicio>> obtenerTodosEjercicios() {
        List<Ejercicio> ejercicios = ejercicioService.obtenerTodosEjercicios();
        return ResponseEntity.ok(ejercicios);
    }

    // Obtener ejercicios de un simulacro específico
    @GetMapping("/simulacro/{idSimulacro}")
    public ResponseEntity<List<Ejercicio>> obtenerEjerciciosPorSimulacro(@PathVariable Long idSimulacro) {
        List<Ejercicio> ejercicios = ejercicioService.obtenerEjerciciosPorSimulacro(idSimulacro);
        return ResponseEntity.ok(ejercicios);
    }

    // Modificar el nombre de un ejercicio
    @PutMapping("/modificarNombre/{id}")
    public ResponseEntity<Ejercicio> modificarNombreEjercicio(
            @PathVariable Long id,
            @RequestParam String nuevoNombre
    ) {
        Ejercicio ejercicioModificado = ejercicioService.modificarNombreEjercicio(id, nuevoNombre);
        return ResponseEntity.ok(ejercicioModificado);
    }

    // Modificar la marca de un ejercicio
    @PutMapping("/modificarMarca/{id}")
    public ResponseEntity<Ejercicio> modificarMarcaEjercicio(
            @PathVariable Long id,
            @RequestParam double nuevaMarca
    ) {
        Ejercicio ejercicioModificado = ejercicioService.modificarMarcaEjercicio(id, nuevaMarca);
        return ResponseEntity.ok(ejercicioModificado);
    }

    // Eliminar un ejercicio
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEjercicio(@PathVariable Long id) {
        ejercicioService.eliminarEjercicio(id);
        return ResponseEntity.noContent().build();
    }
}
