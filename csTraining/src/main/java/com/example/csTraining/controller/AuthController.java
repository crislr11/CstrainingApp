package com.example.csTraining.controller;

import com.example.csTraining.controller.models.AuthResponse;
import com.example.csTraining.controller.models.AuthenticationRequest;
import com.example.csTraining.controller.models.RegisterRequest;
import com.example.csTraining.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    @Qualifier("authService")
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registro(@RequestBody RegisterRequest request){

        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthenticationRequest request) {
        try {
            // Intentar autenticar y devolver el token JWT
            return ResponseEntity.ok(authService.authenticate(request));
        } catch (UsernameNotFoundException ex) {
            // Si no se encuentra el usuario
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new AuthResponse("Usuario no encontrado: " + ex.getMessage()));
        } catch (RuntimeException ex) {
            // Si las credenciales son inválidas u ocurre otro error
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse("Credenciales inválidas: " + ex.getMessage()));
        } catch (Exception ex) {
            // Capturar cualquier otro tipo de error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse("Error inesperado: " + ex.getMessage()));
        }
    }
}
