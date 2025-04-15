package com.example.csTraining.controller;

import com.example.csTraining.controller.models.AuthResponse;
import com.example.csTraining.controller.models.AuthenticationRequest;
import com.example.csTraining.controller.models.RegisterRequest;
import com.example.csTraining.exceptions.CustomAuthenticationException;
import com.example.csTraining.exceptions.UserAlreadyExistsException;
import com.example.csTraining.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    @Qualifier("authService")
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registro(@RequestBody RegisterRequest request) {
        try {
            AuthResponse authResponse = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
        } catch (UserAlreadyExistsException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse(ex.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        try {
            AuthResponse response = authService.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (CustomAuthenticationException ex) {
            return ResponseEntity.status(ex.getStatus())
                    .body(Collections.singletonMap("error", ex.getMessage()));
        }
    }
}
