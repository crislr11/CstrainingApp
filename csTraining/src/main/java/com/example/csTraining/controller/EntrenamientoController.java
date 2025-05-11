package com.example.csTraining.controller;

import com.example.csTraining.controller.DTO.EntrenamientoDTO;
import com.example.csTraining.controller.DTO.EntrenamientoResponseDTO;
import com.example.csTraining.entity.enums.Oposicion;
import com.example.csTraining.entity.User;
import com.example.csTraining.service.EntrenamientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/entrenamientos")
@RequiredArgsConstructor
public class EntrenamientoController {

    @Autowired
    @Qualifier("entrenamientoService")
    private final EntrenamientoService entrenamientoService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createTraining(@RequestBody EntrenamientoDTO entrenamientoDTO) {
        try {
            EntrenamientoResponseDTO nuevoEntrenamiento = entrenamientoService.createTraining(entrenamientoDTO);
            return new ResponseEntity<>(nuevoEntrenamiento, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear el entrenamiento: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','PROFESOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTraining(@PathVariable Long id, @AuthenticationPrincipal User user) {
        try {
            entrenamientoService.deleteTraining(id, user);
            return ResponseEntity.ok("Entrenamiento eliminado correctamente.");
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','PROFESOR')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTraining(@PathVariable Long id,
                                            @RequestBody EntrenamientoDTO entrenamientoDTO,
                                            @AuthenticationPrincipal User user) {
        try {
            EntrenamientoResponseDTO updatedEntrenamiento = entrenamientoService.updateTraining(id, entrenamientoDTO, user);
            return ResponseEntity.ok(updatedEntrenamiento);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllTrainings() {
        try {
            List<EntrenamientoResponseDTO> entrenamientos = entrenamientoService.getAllTrainings();
            return ResponseEntity.ok(entrenamientos);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESOR')")
    @GetMapping("/profesor/{id}")
    public ResponseEntity<?> getTrainingsByProfessor(@PathVariable Long id) {
        try {
            List<EntrenamientoResponseDTO> entrenamientos = entrenamientoService.getTrainingsByProfessor(id);
            return ResponseEntity.ok(entrenamientos);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('OPOSITOR')")
    @GetMapping("/oposicion/{oposicion}")
    public ResponseEntity<?> getTrainingsByOpposition(@PathVariable Oposicion oposicion) {
        try {
            List<EntrenamientoResponseDTO> entrenamientos = entrenamientoService.getTrainingsByOpposition(oposicion);
            return ResponseEntity.ok(entrenamientos);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESOR') or hasRole('OPOSITOR')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getTrainingById(@PathVariable Long id) {
        try {
            EntrenamientoResponseDTO entrenamiento = entrenamientoService.getTrainingById(id);
            return ResponseEntity.ok(entrenamiento);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('OPOSITOR')")
    @GetMapping("/futurosEntrenos/{oposicion}")
    public ResponseEntity<?> getFutureTrainingsByOpposition(@PathVariable Oposicion oposicion) {
        try {
            LocalDateTime fechaActual = LocalDateTime.now();
            List<EntrenamientoResponseDTO> entrenamientos = entrenamientoService.getFutureTrainingsByOpposition(oposicion, fechaActual);
            return ResponseEntity.ok(entrenamientos);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/filtrar-por-fechas")
    public ResponseEntity<?> getEntrenamientosEntreFechas(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        try {
            List<EntrenamientoResponseDTO> entrenamientos = entrenamientoService.obtenerEntrenamientosEntreFechas(inicio, fin);
            return ResponseEntity.ok(entrenamientos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Ocurrió un error inesperado al filtrar entrenamientos.",
                    "detalles", e.getMessage()
            ));
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESOR')")
    @GetMapping("/profesor/{id}/futuros")
    public ResponseEntity<?> getFutureTrainingsByProfessor(@PathVariable Long id) {
        try {
            List<EntrenamientoResponseDTO> entrenamientos = entrenamientoService.getFutureTrainingsByProfessor(id);
            return ResponseEntity.ok(entrenamientos);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

}