package com.example.csTraining.service;

import com.example.csTraining.controller.DTO.EntrenamientoInscripcionResponse;
import com.example.csTraining.controller.DTO.EntrenamientoResponseDTO;
import com.example.csTraining.controller.DTO.EntrenamientoResponseOpositor;

import java.util.List;

public interface OpositorService {
    EntrenamientoInscripcionResponse apuntarseEntrenamiento(Long entrenamientoId, Long userId);
    EntrenamientoInscripcionResponse desapuntarseEntrenamiento(Long entrenamientoId, Long userId);
    List<EntrenamientoResponseOpositor> obtenerEntrenamientosDelOpositor(Long userId);
}
