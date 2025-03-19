package com.example.csTraining.repository;

import com.example.csTraining.entity.Class;
import com.example.csTraining.entity.Oposion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<Class, Long> {
    
    List<Class> findByOposicion(Oposion oposicion); // Filtrar por oposición
}

