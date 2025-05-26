package com.example.csTraining.controller.DTO.response;

import com.example.csTraining.controller.DTO.UserDTO;
import com.example.csTraining.entity.enums.Lugar;
import com.example.csTraining.entity.enums.Oposicion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntrenamientoResponseDTO {
    private Long id;
    private Oposicion oposicion;
    private List<UserDTO> profesores;  // Lista de profesores (UserDTO)
    private List<UserDTO> alumnos;     // Lista de alumnos (UserDTO)
    private LocalDateTime fecha;       // Fecha del entrenamiento
    private Lugar lugar;               // Lugar del entrenamiento
}
