package com.example.csTraining.controller.models;

import com.example.csTraining.entity.Pago;
import com.example.csTraining.entity.simulacros.Simulacro;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private Long id;
    private String email;
    private String oposicion;
    private String role;
    private String nombreUsuario;
    private int creditos;
    private boolean pagado;
    private List<Pago> pagos;
    private List<Simulacro> simulacros;


    // Constructorpara manejar (error)
    public AuthResponse(String token) {
        this.token = token;
    }
}
