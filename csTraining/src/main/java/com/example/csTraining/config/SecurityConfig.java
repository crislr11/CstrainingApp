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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(
                "http://35.181.152.177:4200",
                "http://127.0.0.1:4200",
                "http://localhost:4200"
        ));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L); // 1 hora para cache preflight

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}