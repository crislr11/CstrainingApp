package com.example.csTraining.repository;

import com.example.csTraining.entity.Entrenamiento;
import com.example.csTraining.entity.Oposicion;
import com.example.csTraining.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository("entrenamientoRepository")
public interface EntrenamientoRepository extends JpaRepository<Entrenamiento, Long> {

    // Buscar entrenamientos por profesor
    List<Entrenamiento> findByProfesoresContains(User profesor);

    // Buscar entrenamientos por oposición
    List<Entrenamiento> findByOposicion(Oposicion oposicion);

    List<Entrenamiento> findByProfesores(User professor);

    List<Entrenamiento> findByOposicionAndFechaAfter(Oposicion oposicion, LocalDateTime fecha);

    List<Entrenamiento> findByProfesoresAndFechaAfter(User profesor, LocalDateTime fecha);


}


