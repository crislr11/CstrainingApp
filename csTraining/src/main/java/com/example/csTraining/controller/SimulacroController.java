package com.example.csTraining.controller;


import com.example.csTraining.controller.DTO.request.EjercicioMarcaRequestDTO;
import com.example.csTraining.controller.DTO.request.SimulacroRequestDTO;
import com.example.csTraining.controller.DTO.response.SimulacroResponseDTO;
import com.example.csTraining.exceptions.ResourceNotFoundException;
import com.example.csTraining.service.simulacro.SimulacroService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/simulacro")
@RequiredArgsConstructor
public class SimulacroController {

    @Autowired
    @Qualifier("simulacroService")
    private SimulacroService simulacroService;

    // Obtener todos los simulacros de un usuario
    @PreAuthorize("hasAnyRole('OPOSITOR','PROFESOR')")
    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<SimulacroResponseDTO>> getSimulacrosPorUsuario(@PathVariable Long userId) {
        List<SimulacroResponseDTO> simulacros = simulacroService.getSimulacrosPorUsuario(userId);
        return ResponseEntity.ok(simulacros);
    }

    // Crear o actualizar un simulacro
    @PreAuthorize("hasRole('PROFESOR')")
    @PostMapping
    public ResponseEntity<SimulacroResponseDTO> saveSimulacro(@RequestBody SimulacroRequestDTO simulacroRequestDTO) {
        SimulacroResponseDTO savedSimulacro = simulacroService.saveSimulacro(simulacroRequestDTO);
        return ResponseEntity.ok(savedSimulacro);
    }

    // Eliminar un simulacro
    @PreAuthorize("hasRole('PROFESOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSimulacro(@PathVariable Long id) {
        simulacroService.eliminarSimulacro(id);
        return ResponseEntity.noContent().build();
    }

    // Asignar un simulacro a un usuario
    @PreAuthorize("hasRole('PROFESOR')")
    @PutMapping("/añadir/{simulacroId}/usuario/{userId}")
    public ResponseEntity<SimulacroResponseDTO> asignarSimulacroAUsuario(
            @PathVariable Long simulacroId,
            @PathVariable Long userId) {

        SimulacroResponseDTO simulacroActualizado = simulacroService.asignarSimulacroAUsuario(simulacroId, userId);

        if (simulacroActualizado == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(simulacroActualizado);
    }

    // Agregar un ejercicio a un simulacro
    @PreAuthorize("hasRole('PROFESOR')")
    @PostMapping("/{simulacroId}/agregar-ejercicio")
    public ResponseEntity<SimulacroResponseDTO> agregarEjercicioASimulacro(
            @PathVariable Long simulacroId,
            @RequestBody EjercicioMarcaRequestDTO nuevoEjercicioDTO) {
        try {
            System.out.println("Agregando ejercicio " + nuevoEjercicioDTO);
            SimulacroResponseDTO simulacroActualizado = simulacroService.agregarEjercicioASimulacro(simulacroId, nuevoEjercicioDTO);
            return ResponseEntity.ok(simulacroActualizado);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}
