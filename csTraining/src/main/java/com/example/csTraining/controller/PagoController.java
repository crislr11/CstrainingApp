package com.example.csTraining.controller;

import com.example.csTraining.controller.DTO.PagoDTO;
import com.example.csTraining.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    @Qualifier("pagoService")
    private PagoService pagoService;


    // Crear un pago para un usuario
    @PostMapping("/{userId}")
    public ResponseEntity<String> crearPago(@PathVariable Long userId) {
        try {
            String mensaje = pagoService.crearPago(userId);
            return new ResponseEntity<>(mensaje, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al registrar el pago: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener historial de pagos de un usuario
    @GetMapping("/{userId}/historial")
    public ResponseEntity<List<PagoDTO>> obtenerHistorialPagos(@PathVariable Long userId) {
        try {
            List<PagoDTO> historialPagos = pagoService.obtenerHistorialPagos(userId);
            System.out.println(historialPagos);
            return new ResponseEntity<>(historialPagos, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Eliminar un pago por su ID
    @DeleteMapping("/{pagoId}")
    public ResponseEntity<String> eliminarPago(@PathVariable Long pagoId) {
        try {
            String mensaje = pagoService.eliminarPago(pagoId);
            return new ResponseEntity<>(mensaje, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar el pago: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Restablecer el estado de los pagos (lo que se ejecuta al final del mes)
    @PutMapping("/restablecer")
    public ResponseEntity<String> restablecerPagos() {
        try {
            pagoService.restablecerEstadoPagado();
            return new ResponseEntity<>("Pagos restablecidos a 'no pagado'.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al restablecer pagos: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
