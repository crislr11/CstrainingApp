package com.example.csTraining.repository.simulacros;
import com.example.csTraining.entity.simulacros.Ejercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("ejercicioRepository")
public interface EjercicioRepository extends JpaRepository<Ejercicio, Long> {

    // Buscar ejercicio por nombre
    Ejercicio findByNombre(String nombre);
}
