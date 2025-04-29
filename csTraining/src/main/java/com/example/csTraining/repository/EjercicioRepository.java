package com.example.csTraining.repository;

import com.example.csTraining.entity.Ejercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ejercicioRepository")
public interface EjercicioRepository extends JpaRepository<Ejercicio, Long> {

    List<Ejercicio> findBySimulacroId(Long simulacroId);
}
