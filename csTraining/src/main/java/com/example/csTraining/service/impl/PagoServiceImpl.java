package com.example.csTraining.service.impl;

import com.example.csTraining.controller.models.PagoDTO;
import com.example.csTraining.entity.Pago;
import com.example.csTraining.entity.User;
import com.example.csTraining.repository.PagoRepository;
import com.example.csTraining.repository.UserRepository;
import com.example.csTraining.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service("pagoService")
public class PagoServiceImpl implements PagoService {

    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;

    @Autowired
    @Qualifier("pagoRepository")
    private PagoRepository pagoRepository;


    // Crear un pago para el usuario
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public String crearPago(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con ID: " + userId));

        // Verificar si ya ha pagado este mes (1-10)
        LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
        LocalDate finMes = LocalDate.now().withDayOfMonth(10);

        if (pagoRepository.existsByUserIdAndFechaPagoBetween(userId, inicioMes, finMes)) {
            return "Este usuario ya ha pagado este mes.";
        }

        // Crear el pago
        Pago pago = new Pago();
        pago.setUser(user);
        pago.setFechaPago(LocalDate.now());
        pago.setMonto(35.0);

        pago.setUser(user);

        pagoRepository.save(pago);
        actualizarCreditosPorPago(userId);

        return "Pago registrado correctamente.";
    }


    // Obtener el historial de pagos de un usuario
    @PreAuthorize("hasRole('ADMIN')")
    public List<PagoDTO> obtenerHistorialPagos(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con ID: " + userId));

        List<Pago> pagos = user.getPagos(); // o si usas JPA: pagoRepository.findByUserId(userId);

        return pagos.stream()
                .map(pago -> new PagoDTO(pago.getId(), pago.getMonto(), pago.getFechaPago(),pago.getUser().getId()))
                .collect(Collectors.toList());
    }

    // Eliminar un pago por su ID
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminarPago(Long pagoId) {
        Pago pago = pagoRepository.findById(pagoId)
                .orElseThrow(() -> new NoSuchElementException("Pago no encontrado con ID: " + pagoId));

        pagoRepository.delete(pago);
        return "Pago eliminado correctamente.";
    }

    // Actualizar los créditos de un usuario después de un pago
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void actualizarCreditosPorPago(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con ID: " + userId));

        user.setCreditos(14);
        user.setPagado(true);
        userRepository.save(user);
    }

    // Restablecer el estado de todos los usuarios a "no pagado" al final de cada mes
    @Override
    public void restablecerEstadoPagado() {
        List<User> usuarios = userRepository.findAll();
        for (User user : usuarios) {
            user.setPagado(false);
            user.setCreditos(0);
            userRepository.save(user);
        }
    }
}
