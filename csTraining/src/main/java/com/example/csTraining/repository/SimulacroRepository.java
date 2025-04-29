package com.example.csTraining.repository;

import com.example.csTraining.entity.Simulacro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("simulacroRepository")
public interface SimulacroRepository extends JpaRepository<Simulacro, Long> {
    List<Simulacro> findByUserId(Long userId);
}
