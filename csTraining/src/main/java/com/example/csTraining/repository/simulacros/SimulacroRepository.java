package com.example.csTraining.repository.simulacros;


import com.example.csTraining.entity.User;
import com.example.csTraining.entity.simulacros.Simulacro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository("simulacroRepository")
public interface SimulacroRepository extends JpaRepository<Simulacro, Long> {

    // Obtener todos los simulacros de un usuario
    List<Simulacro> findByUser(User user);

    // Obtener simulacros por fecha
    List<Simulacro> findByFechaBetween(LocalDate startDate, LocalDate endDate);
}
