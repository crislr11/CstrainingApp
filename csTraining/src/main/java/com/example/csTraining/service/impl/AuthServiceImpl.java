package com.example.csTraining.service.impl;

import com.example.csTraining.config.JwtService;
import com.example.csTraining.controller.models.AuthResponse;
import com.example.csTraining.controller.models.AuthenticationRequest;
import com.example.csTraining.controller.models.RegisterRequest;
import com.example.csTraining.entity.User;
import com.example.csTraining.repository.UserRepository;
import com.example.csTraining.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("authService")
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse authenticate(AuthenticationRequest request) {
        // Intentamos autenticar al usuario con Spring Security.
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            // Una vez autenticado, recuperamos el usuario
            User user = (User) authentication.getPrincipal();

            // Generamos el JWT token para el usuario autenticado
            var jwtToken = jwtService.generateJwtToken(user);

            return AuthResponse.builder().token(jwtToken).build();

        } catch (BadCredentialsException e) {
            // Si las credenciales no son válidas, lanzamos una excepción.
            throw new RuntimeException("Credenciales inválidas", e);
        } catch (Exception e) {
            // Cualquier otro error, lanzamos una excepción genérica.
            e.printStackTrace();
            throw new RuntimeException("Error en el proceso de autenticación", e);
        }
    }


    @Override
    public AuthResponse register(RegisterRequest registerRequest) {
        Optional<User> existingUser = userRepository.findByEmail(registerRequest.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("El usuario con este correo ya existe");
        }


        var user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole())
                .oposion(registerRequest.getOposion())
                .build();

        userRepository.save(user);
        var jwtToken = jwtService.generateJwtToken(user);
        return AuthResponse.builder().token(jwtToken).build();
    }
}
