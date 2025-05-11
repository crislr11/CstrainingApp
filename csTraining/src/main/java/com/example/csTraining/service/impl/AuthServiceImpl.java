package com.example.csTraining.service.impl;

import com.example.csTraining.config.JwtService;
import com.example.csTraining.controller.models.AuthResponse;
import com.example.csTraining.controller.models.AuthenticationRequest;
import com.example.csTraining.controller.models.RegisterRequest;
import com.example.csTraining.entity.User;
import com.example.csTraining.entity.simulacros.Simulacro;
import com.example.csTraining.exceptions.AccountDisabledException;
import com.example.csTraining.exceptions.CustomAuthenticationException;
import com.example.csTraining.exceptions.UserAlreadyExistsException;
import com.example.csTraining.repository.UserRepository;
import com.example.csTraining.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public AuthResponse authenticate(AuthenticationRequest request) {

        try {
            // Autenticación del usuario
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            // Obtener el usuario autenticado
            User user = (User) authentication.getPrincipal();

            // Verificar si el usuario está activo
            if (!user.isActive()) {
                throw new AccountDisabledException("Tu cuenta no está activada. Contacta con el administrador.");
            }

            // Generar el token JWT
            String jwtToken = jwtService.generateJwtToken(user);

            // Construir la respuesta de autenticación con los simulacros
            return AuthResponse.builder()
                    .token(jwtToken)
                    .id(user.getId())
                    .email(user.getUsername())
                    .oposicion(user.getOposicion().name())
                    .role(user.getRole().name())
                    .nombreUsuario(user.getNombreUsuario())
                    .creditos(user.getCreditos())
                    .pagado(user.isPagado())
                    .pagos(user.getPagos())
                    .simulacros(user.getSimulacros())
                    .build();

        } catch (UsernameNotFoundException e) {
            throw new CustomAuthenticationException("Usuario no encontrado: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BadCredentialsException e) {
            throw new CustomAuthenticationException("Credenciales incorrectas. Revisa tu email y contraseña.", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            throw new CustomAuthenticationException("Tu usuario está desactivado, espera a que el administrador te active", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @Override
    public AuthResponse register(RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("El usuario con este correo ya existe.");
        }

        var user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole())
                .oposicion(registerRequest.getOposicion())
                .build();

        userRepository.save(user);
        var jwtToken = jwtService.generateJwtToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .email(user.getUsername())
                .oposicion(user.getOposicion().name())
                .role(user.getRole().name())
                .build();
    }

}
