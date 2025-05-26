package com.example.csTraining.entity;

import com.example.csTraining.entity.enums.Oposicion;
import com.example.csTraining.entity.enums.Role;
import com.example.csTraining.entity.simulacros.Simulacro;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "usuarios")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean isActive;

    private String token;

    @Enumerated(EnumType.STRING)
    private Oposicion oposicion;

    private int creditos;

    private boolean pagado;

    private String foto;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pago> pagos = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Simulacro> simulacros = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "user_ejercicio_marcas", joinColumns = @JoinColumn(name = "user_id"))
    private List<MarcaOpositor> marcasOpositor = new ArrayList<>();


    // Implementación de UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_" + role.name());
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getNombreUsuario() {
        return username;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.username = nombreUsuario;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
