package com.example.csTraining.service;

import com.example.csTraining.controller.models.PagoDTO;
import com.example.csTraining.entity.Pago;
import java.util.List;

public interface PagoService {

    // Crear un pago
    String crearPago(Long userId);

    // Obtener el historial de pagos de un usuario
    List<PagoDTO> obtenerHistorialPagos(Long userId);

    // Eliminar un pago de un usuario
    String eliminarPago(Long pagoId);

    // Método que se ejecuta al final de cada mes para cambiar el estado de todos los usuarios a no pagado
    void restablecerEstadoPagado();

    // Actualizar los créditos de los usuarios que hayan pagado
    void actualizarCreditosPorPago(Long userId);
}
