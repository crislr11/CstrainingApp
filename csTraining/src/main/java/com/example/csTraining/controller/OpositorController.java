package com.example.csTraining.controller;

import com.example.csTraining.controller.DTO.MarcaOpositorDTO;
import com.example.csTraining.controller.DTO.response.EntremientoOpositorRequest;
import com.example.csTraining.controller.DTO.response.EntrenamientoInscripcionResponse;
import com.example.csTraining.controller.DTO.response.EntrenamientoResponseOpositor;
import com.example.csTraining.exceptions.UserNotFoundException;
import com.example.csTraining.service.OpositorService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

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

    @PostMapping
    public ResponseEntity<?> addMarca(@RequestBody MarcaOpositorDTO dto) {
        try {
            opositorService.addMarca(dto);
            return ResponseEntity.ok("Marca añadida correctamente.");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al añadir marca: " + e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> removeMarca(@RequestBody MarcaOpositorDTO dto) {
        try {
            opositorService.removeMarca(dto);
            return ResponseEntity.ok("Marca eliminada correctamente.");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado: " + e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Marca no encontrada: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar marca: " + e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getMarcasPorFecha(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta
    ) {
        try {
            List<MarcaOpositorDTO> marcas = opositorService.getMarcasPorFecha(userId, desde, hasta);
            return ResponseEntity.ok(marcas);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener marcas: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('OPOSITOR')")
    @GetMapping("/{userId}/todas")
    public ResponseEntity<?> getTodasLasMarcas(@PathVariable Long userId) {
        try {
            List<MarcaOpositorDTO> marcas = opositorService.getTodasLasMarcas(userId);
            return ResponseEntity.ok(marcas);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener marcas: " + e.getMessage());
        }
    }

    @GetMapping("/{userId}/ejercicio/{ejercicioId}")
    public ResponseEntity<?> getMarcasPorEjercicio(@PathVariable Long userId, @PathVariable Long ejercicioId) {
        try {
            List<MarcaOpositorDTO> marcas = opositorService.getMarcasPorEjercicio(userId, ejercicioId);
            return ResponseEntity.ok(marcas);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener marcas: " + e.getMessage());
        }
    }

    @PostMapping("/usuarios/{id}/foto")
    public ResponseEntity<?> uploadUserPhoto(@PathVariable Long id, @RequestParam("foto") MultipartFile foto) {
        try {
            opositorService.uploadUserPhoto(id, foto);
            return ResponseEntity.ok("Foto subida correctamente");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado");
        }
    }

    @GetMapping("/usuarios/foto/{filename:.+}")
    public ResponseEntity<?> getUserPhoto(@PathVariable String filename) {
        try {
            return opositorService.getUserPhoto(filename);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al cargar la foto");
        }
    }

}
