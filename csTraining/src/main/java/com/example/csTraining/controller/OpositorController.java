package com.example.csTraining.controller;

import com.example.csTraining.controller.DTO.EntremientoOpositorRequest;
import com.example.csTraining.controller.DTO.EntrenamientoInscripcionResponse;
import com.example.csTraining.controller.DTO.EntrenamientoResponseDTO;
import com.example.csTraining.controller.DTO.EntrenamientoResponseOpositor;
import com.example.csTraining.exceptions.UserNotFoundException;
import com.example.csTraining.service.OpositorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/opositor")
@RequiredArgsConstructor
public class OpositorController {

    private final OpositorService opositorService;

    @PostMapping("/inscripciones/apuntar")
    @PreAuthorize("hasRole('OPOSITOR')")
    public ResponseEntity<?> apuntarseAEntrenamiento(@RequestBody EntremientoOpositorRequest request) {
        try {
            opositorService.apuntarseEntrenamiento(request.getEntrenamientoId(), request.getUserId());
            return ResponseEntity.ok("¡Te has apuntado al entrenamiento correctamente!");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado: " + e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Atención: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("¡Ups! Algo salió mal: " + e.getMessage());
        }
    }

    @PostMapping("/inscripciones/desapuntar")
    @PreAuthorize("hasRole('OPOSITOR')")
    public ResponseEntity<?> desapuntarseDeEntrenamiento(@RequestBody EntrenamientoInscripcionResponse request) {
        try {
            opositorService.desapuntarseEntrenamiento(request.getEntrenamientoId(), request.getUserId());
            return ResponseEntity.ok("¡Te has desapuntado del entrenamiento correctamente!");
        } catch (UserNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Usuario no encontrado: " + e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Atención: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("¡Ups! Algo salió mal: " + e.getMessage());
        }
    }

    @GetMapping("/entrenamientos/{userId}")
    @PreAuthorize("hasRole('OPOSITOR')")
    public ResponseEntity<?> getEntrenamientosDelOpositor(@PathVariable Long userId) {
        try {
            List<EntrenamientoResponseOpositor> entrenamientos = opositorService.obtenerEntrenamientosDelOpositor(userId);
            return ResponseEntity.ok(entrenamientos);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("¡Ups! " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("¡Ups! Ocurrió un error al obtener los entrenamientos.");
        }
    }

}
