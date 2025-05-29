package com.example.csTraining.service;

import com.example.csTraining.controller.DTO.MarcaOpositorDTO;
import com.example.csTraining.controller.DTO.response.EntrenamientoInscripcionResponse;
import com.example.csTraining.controller.DTO.response.EntrenamientoResponseOpositor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
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
    void uploadUserPhoto(Long userId, MultipartFile foto) throws IOException;
    ResponseEntity<Resource> getUserPhoto(String filename) throws MalformedURLException, FileNotFoundException;


}
