package com.example.csTraining.repository;

import com.example.csTraining.controller.models.PagoDTO;
import com.example.csTraining.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository("pagoRepository")
public interface PagoRepository extends JpaRepository<Pago, Long> {

    // Método para encontrar todos los pagos de un usuario
    List<Pago> findByUserId(Long userId);

    // Método para verificar si ya hay un pago registrado en un rango de fechas
    boolean existsByUserIdAndFechaPagoBetween(Long userId, LocalDate startDate, LocalDate endDate);

    // Método para eliminar pagos por usuario
    void deleteByUserId(Long userId);
}
