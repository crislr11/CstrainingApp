package com.example.csTraining.controller;

import com.example.csTraining.controller.DTO.UsuarioRankingDTO;
import com.example.csTraining.entity.enums.Oposicion;
import com.example.csTraining.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/ranking")
@RequiredArgsConstructor
public class RankingController {

    private final RankingService rankingService;

    @GetMapping("/ejercicio")
    public ResponseEntity<List<UsuarioRankingDTO>> obtenerRankingEjercicio(
            @RequestParam Long ejercicioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam Oposicion oposicion,
            @RequestParam boolean esTiempo) {

        if (ejercicioId == null || fecha == null || oposicion == null) {
            return ResponseEntity.badRequest().build();
        }

        List<UsuarioRankingDTO> ranking = rankingService.obtenerRankingEjercicioPorFechaYOposicion(ejercicioId, fecha, oposicion, esTiempo);

        return ResponseEntity.ok(ranking); // siempre 200
    }


    @GetMapping("/fechas")
    public ResponseEntity<List<LocalDate>> obtenerFechasSimulacros(
            @RequestParam Oposicion oposicion) {

        if (oposicion == null) {
            return ResponseEntity.badRequest().build();
        }

        List<LocalDate> fechas = rankingService.obtenerFechasSimulacrosPorOposicion(oposicion);

        if (fechas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(fechas);
    }
}