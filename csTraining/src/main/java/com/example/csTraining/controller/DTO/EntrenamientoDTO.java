package com.example.csTraining.controller.DTO;

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
public class EntrenamientoDTO {
    private Long id;
    private Oposicion oposicion;
    private List<UserDTO> profesores;
    private List<UserDTO> alumnos;
    private LocalDateTime fecha;
    private Lugar lugar;
}
