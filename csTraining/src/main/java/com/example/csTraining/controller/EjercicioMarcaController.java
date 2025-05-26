package com.example.csTraining.controller;

import com.example.csTraining.controller.DTO.response.EjercicioMarcaRequestDTO;
import com.example.csTraining.controller.DTO.response.EjercicioMarcaResponseDTO;
import com.example.csTraining.service.simulacro.EjercicioMarcaService;
import com.example.csTraining.service.simulacro.EjercicioService;
import com.example.csTraining.service.simulacro.SimulacroService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ejercicio-marca")
@RequiredArgsConstructor
public class EjercicioMarcaController {

    @Autowired
    @Qualifier("ejercicioMarcaService")
    private EjercicioMarcaService ejercicioMarcaService;

    @Autowired
    @Qualifier("ejercicioService")
    private EjercicioService ejercicioService;

    @Autowired
    private SimulacroService simulacroService;

    // Obtener todas las marcas de un ejercicio - Solo para PROFESORES
    @PreAuthorize("hasRole('PROFESOR')")
    @GetMapping("/marcas/{ejercicioId}")
    public ResponseEntity<List<EjercicioMarcaResponseDTO>> getMarcasPorEjercicio(@PathVariable Long ejercicioId) {
        List<EjercicioMarcaResponseDTO> marcas = ejercicioMarcaService.listarMarcasPorEjercicio(ejercicioId);
        return ResponseEntity.ok(marcas);
    }

    // Obtener el ranking (top 5) por ejercicio - Solo para OPOSITORES
    @PreAuthorize("hasRole('OPOSITOR')")
    @GetMapping("/ranking/{ejercicioId}")
    public ResponseEntity<List<EjercicioMarcaResponseDTO>> getTop5PorEjercicio(@PathVariable Long ejercicioId) {
        List<EjercicioMarcaResponseDTO> top5 = ejercicioMarcaService.listarTop5PorEjercicio(ejercicioId);
        return ResponseEntity.ok(top5);
    }

    // Obtener las marcas de un simulacro - Solo para PROFESORES
    @PreAuthorize("hasRole('PROFESOR')")
    @GetMapping("/simulacro/{simulacroId}")
    public ResponseEntity<List<EjercicioMarcaResponseDTO>> getMarcasPorSimulacro(@PathVariable Long simulacroId) {
        List<EjercicioMarcaResponseDTO> marcas = ejercicioMarcaService.listarMarcasPorSimulacro(simulacroId);
        return ResponseEntity.ok(marcas);
    }

    // Guardar una nueva marca - Solo para PROFESORES
    @PreAuthorize("hasRole('PROFESOR')")
    @PostMapping("/marca")
    public ResponseEntity<EjercicioMarcaResponseDTO> saveMarca(@RequestBody EjercicioMarcaRequestDTO ejercicioMarca) {
        EjercicioMarcaResponseDTO savedMarca = ejercicioMarcaService.guardarMarca(ejercicioMarca);
        return ResponseEntity.ok(savedMarca);
    }

    // Eliminar una marca - Solo para PROFESORES
    @PreAuthorize("hasRole('PROFESOR')")
    @DeleteMapping("/marca/{id}")
    public ResponseEntity<Void> deleteMarca(@PathVariable Long id) {
        ejercicioMarcaService.eliminarMarca(id);
        return ResponseEntity.noContent().build();
    }
}
