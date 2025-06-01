package com.example.csTraining.service.impl;

import com.example.csTraining.controller.DTO.UsuarioRankingDTO;
import com.example.csTraining.entity.User;
import com.example.csTraining.entity.enums.Oposicion;
import com.example.csTraining.entity.simulacros.EjercicioMarca;
import com.example.csTraining.entity.simulacros.Simulacro;
import com.example.csTraining.repository.UserRepository;
import com.example.csTraining.repository.simulacros.SimulacroRepository;
import com.example.csTraining.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RankingServiceImpl implements RankingService {
    @Autowired
    @Qualifier("simulacroRepository")
    private SimulacroRepository simulacroRepository;

    @Autowired
    @Qualifier("userRepository")
    private final UserRepository userRepository;

    @Override
    public List<UsuarioRankingDTO> obtenerRankingEjercicioPorFechaYOposicion(
            Long ejercicioId,
            LocalDate fecha,
            Oposicion oposicion,
            boolean esTiempo) {

        try {
            List<User> usuarios = userRepository.findByOposicion(oposicion);
            if (usuarios.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No se encontraron usuarios para la oposición especificada");
            }

            List<UsuarioRankingDTO> todos = new ArrayList<>();

            for (User user : usuarios) {
                for (Simulacro simulacro : user.getSimulacros()) {
                    if (simulacro.getFecha().equals(fecha)) {
                        for (EjercicioMarca ejercicioMarca : simulacro.getEjercicios()) {
                            if (ejercicioMarca.getEjercicio().getId().equals(ejercicioId)) {
                                todos.add(new UsuarioRankingDTO(
                                        user.getNombreUsuario(),
                                        ejercicioMarca.getMarca()
                                ));
                            }
                        }
                    }
                }
            }

            // Agrupar por usuario y conservar solo la mejor marca
            Map<String, UsuarioRankingDTO> mejorPorUsuario = todos.stream()
                    .collect(Collectors.toMap(
                            UsuarioRankingDTO::getNombreUsuario,
                            dto -> dto,
                            (dto1, dto2) -> esTiempo
                                    ? (dto1.getMarca() < dto2.getMarca() ? dto1 : dto2)
                                    : (dto1.getMarca() > dto2.getMarca() ? dto1 : dto2)
                    ));

            List<UsuarioRankingDTO> resultado = new ArrayList<>(mejorPorUsuario.values());

            // Ordenar
            resultado.sort(esTiempo
                    ? Comparator.comparingDouble(UsuarioRankingDTO::getMarca)
                    : Comparator.comparingDouble(UsuarioRankingDTO::getMarca).reversed());

            return resultado;

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al procesar el ranking", e);
        }
    }



    @Override
    public List<LocalDate> obtenerFechasSimulacrosPorOposicion(Oposicion oposicion) {
        try {
            List<User> usuarios = userRepository.findByOposicion(oposicion);

            if (usuarios.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron usuarios para la oposición especificada");
            }

            List<LocalDate> fechas = usuarios.stream()
                    .flatMap(user -> user.getSimulacros().stream())
                    .map(Simulacro::getFecha)
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());

            if (fechas.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron simulacros para la oposición especificada");
            }

            return fechas;

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener las fechas de simulacros", e);
        }
    }
}