package com.example.csTraining.controller;

import com.example.csTraining.entity.Entrenamiento;
import com.example.csTraining.entity.Oposicion;
import com.example.csTraining.entity.User;
import com.example.csTraining.service.EntrenamientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/entrenamientos")
@RequiredArgsConstructor
public class EntrenamientoController {

    @Autowired
    @Qualifier("entrenamientoService")
    private final EntrenamientoService entrenamientoService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createTraining(@RequestBody Entrenamiento training) {
        try {
            Entrenamiento nuevoEntrenamiento = entrenamientoService.createTraining(training);
            return new ResponseEntity<>(nuevoEntrenamiento, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear el entrenamiento: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PROFESOR')")
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

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PROFESOR')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTraining(@PathVariable Long id, @RequestBody Entrenamiento entrenamiento, @AuthenticationPrincipal User user) {
        try {
            entrenamientoService.updateTraining(id, entrenamiento, user);
            return ResponseEntity.ok("Entrenamiento actualizado correctamente.");
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
            List<Entrenamiento> entrenamientos = entrenamientoService.getAllTrainings();
            return ResponseEntity.ok(entrenamientos);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('PROFESOR')")
    @GetMapping("/profesor")
    public ResponseEntity<?> getTrainingsByProfessor(@AuthenticationPrincipal User user) {
        try {
            List<Entrenamiento> entrenamientos = entrenamientoService.getTrainingsByProfessor(user);
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
            List<Entrenamiento> entrenamientos = entrenamientoService.getTrainingsByOpposition(oposicion);
            return ResponseEntity.ok(entrenamientos);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESOR') or hasRole('OPOSITOR')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getTrainingById(@PathVariable Long id) {
        try {
            Entrenamiento entrenamiento = entrenamientoService.getTrainingById(id).orElseThrow();
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
            List<Entrenamiento> entrenamientos = entrenamientoService.getFutureTrainingsByOpposition(oposicion, fechaActual);
            return ResponseEntity.ok(entrenamientos);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }
}
