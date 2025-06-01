package com.example.csTraining.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Public Endpoints
                        .requestMatchers("/api/auth/**").permitAll()

                        // Endpoints de ranking - solo para OPOSITOR
                        .requestMatchers("/api/ranking/**").hasRole("OPOSITOR")

                        // Rutas para ADMIN
                        .requestMatchers("/api/admin/**").hasAnyRole("PROFESOR", "ADMIN")
                        .requestMatchers("/api/entrenamientos").hasAnyRole("PROFESOR", "ADMIN","OPOSITOR")
                        .requestMatchers("/api/pagos/").hasRole("ADMIN")

                        // Acceso de un OPOSITOR a sus simulacros
                        .requestMatchers("/api/simulacro/usuario/**").hasAnyRole("PROFESOR", "OPOSITOR")

                        // Acceso a los simulacros para profesor y opositor
                        .requestMatchers("/api/simulacro/**").hasAnyRole("PROFESOR", "OPOSITOR")

                        // Rutas para PROFESOR y OPOSITOR (Acceso a simulacros y ejercicios)
                        .requestMatchers("/ejercicio/**").hasRole("PROFESOR")

                        .requestMatchers("/api/opositor/usuarios/*/foto").hasAnyRole("ADMIN", "OPOSITOR", "PROFESOR")
                        .requestMatchers("/api/opositor/usuarios/foto/**").permitAll()

                        // Solo usuarios con rol OPOSITOR pueden acceder a estos endpoints
                        .requestMatchers("/api/opositor/**").hasRole("OPOSITOR")

                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}