package com.example.csTraining.controller.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private String nombre;
    private String oposicion;
    private String role;

    // Constructorpara manejar (error)
    public AuthResponse(String token) {
        this.token = token;
    }
}
