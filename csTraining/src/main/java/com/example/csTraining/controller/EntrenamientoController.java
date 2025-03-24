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

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/entrenamientos")
@RequiredArgsConstructor
public class EntrenamientoController {

    @Autowired
    @Qualifier("entrenamientoService")
    private EntrenamientoService entrenamientoService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Entrenamiento> createTraining(@RequestBody Entrenamiento training) {
        try {
            Entrenamiento nuevoEntrenamiento = entrenamientoService.createTraining(training);
            return new ResponseEntity<>(nuevoEntrenamiento, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al crear el entrenamiento: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PROFESOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTraining(@PathVariable Long id, @AuthenticationPrincipal User user) {
        try {
            entrenamientoService.deleteTraining(id, user);
            return ResponseEntity.ok("Entrenamiento eliminado correctamente.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permisos para actualizar este entrenamiento.");
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PROFESOR')")
    public ResponseEntity<String> updateTraining(@PathVariable Long id, @RequestBody Entrenamiento entrenamiento, @AuthenticationPrincipal User user) {
        try {
            Entrenamiento updatedTraining = entrenamientoService.updateTraining(id, entrenamiento,user);
            return ResponseEntity.ok("Entrenamiento actualizado correctamente.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entrenamiento no encontrado.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permisos para actualizar este entrenamiento.");
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllTrainings() {
        try {
            List<Entrenamiento> entrenamientos = entrenamientoService.getAllTrainings();
            return ResponseEntity.ok(entrenamientos);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener los entrenamientos.");
        }
    }

    @GetMapping("/profesor")
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<?> getTrainingsByProfessor(@AuthenticationPrincipal User user) {
        try {
            List<Entrenamiento> entrenamientos = entrenamientoService.getTrainingsByProfessor(user);
            return ResponseEntity.ok(entrenamientos);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado: Solo los profesores pueden ver sus entrenamientos.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener los entrenamientos del profesor.");
        }
    }

    @GetMapping("/oposicion/{oposicion}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPOSITOR')")
    public ResponseEntity<?> getTrainingsByOpposition(@PathVariable Oposicion oposicion, @AuthenticationPrincipal User user) {
        try {
            List<Entrenamiento> entrenamientos = entrenamientoService.getTrainingsByOpposition(oposicion);
            return ResponseEntity.ok(entrenamientos);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESOR') or hasRole('OPOSITOR')")
    public ResponseEntity<?> getTrainingById(@PathVariable Long id) {
        try {
            Entrenamiento entrenamiento = entrenamientoService.getTrainingById(id).orElseThrow();
            return ResponseEntity.ok(entrenamiento);
        }catch (AccessDeniedException e) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }




}
