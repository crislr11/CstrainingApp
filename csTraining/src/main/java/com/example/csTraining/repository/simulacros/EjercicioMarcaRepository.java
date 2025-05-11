package com.example.csTraining.repository.simulacros;


import com.example.csTraining.entity.simulacros.Ejercicio;
import com.example.csTraining.entity.simulacros.EjercicioMarca;
import com.example.csTraining.entity.simulacros.Simulacro;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository("ejercicioMarcaRepository")
public interface EjercicioMarcaRepository extends JpaRepository<EjercicioMarca, Long> {

    // Obtener todas las marcas de un ejercicio
    List<EjercicioMarca> findByEjercicio(Ejercicio ejercicio);

    // Obtener las mejores marcas de un ejercicio ordenadas por marca descendente
    List<EjercicioMarca> findTop5ByEjercicioOrderByMarcaDesc(Ejercicio ejercicio);

    // Obtener las marcas de un simulacro específico
    List<EjercicioMarca> findBySimulacro(Simulacro simulacro);

    // Obtener las marcas de un usuario específico (por ejemplo, rankings)
    List<EjercicioMarca> findBySimulacroUserIdAndEjercicioNombreOrderByMarcaDesc(Long userId, String nombreEjercicio);
}
