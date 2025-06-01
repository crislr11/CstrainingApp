package com.example.csTraining.service;


import com.example.csTraining.controller.DTO.UsuarioRankingDTO;
import com.example.csTraining.entity.enums.Oposicion;

import java.time.LocalDate;
import java.util.List;

public interface RankingService {
    List<UsuarioRankingDTO> obtenerRankingEjercicioPorFechaYOposicion(Long ejercicioId, LocalDate fecha, Oposicion oposicion, boolean esTiempo);
    List<LocalDate> obtenerFechasSimulacrosPorOposicion(Oposicion oposicion);
}