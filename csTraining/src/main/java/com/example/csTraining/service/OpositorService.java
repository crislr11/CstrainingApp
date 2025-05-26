package com.example.csTraining.service;

import com.example.csTraining.controller.DTO.MarcaOpositorDTO;
import com.example.csTraining.controller.DTO.response.EntrenamientoInscripcionResponse;
import com.example.csTraining.controller.DTO.response.EntrenamientoResponseOpositor;

import java.time.LocalDateTime;
import java.util.List;

public interface OpositorService {
    EntrenamientoInscripcionResponse apuntarseEntrenamiento(Long entrenamientoId, Long userId);
    EntrenamientoInscripcionResponse desapuntarseEntrenamiento(Long entrenamientoId, Long userId);
    List<EntrenamientoResponseOpositor> obtenerEntrenamientosDelOpositor(Long userId);
    void addMarca(MarcaOpositorDTO dto);
    void removeMarca(MarcaOpositorDTO dto);
    List<MarcaOpositorDTO> getMarcasPorFecha(Long userId, LocalDateTime desde, LocalDateTime hasta);
    List<MarcaOpositorDTO> getTodasLasMarcas(Long userId);
    List<MarcaOpositorDTO> getMarcasPorEjercicio(Long userId, Long ejercicioId);
}
