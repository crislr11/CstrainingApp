// AdminServiceImpl.java
package com.example.csTraining.service.impl;

import com.example.csTraining.entity.User;
import com.example.csTraining.exceptions.UserNotFoundException;
import com.example.csTraining.repository.UserRepository;
import com.example.csTraining.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service("adminService")
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    // Obtiene todos los usuarios
    @Override
    public List<User> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error al recuperar los usuarios: " + e.getMessage(), e);
        }
    }

    // Busca usuario por ID
    @Override
    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserNotFoundException("Usuario con ID " + id + " no encontrado");
        }
    }

    // Actualiza usuario preservando campos no modificados
    @Override
    @Transactional
    public User updateUser(Long id, User userDetails) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario con ID " + id + " no encontrado"));

        try {
            // Actualizar solo campos no nulos
            if (userDetails.getNombreUsuario() != null) {
                existingUser.setNombreUsuario(userDetails.getNombreUsuario());
            }
            if (userDetails.getEmail() != null) {
                existingUser.setEmail(userDetails.getEmail());
            }
            if (userDetails.getRole() != null) {
                existingUser.setRole(userDetails.getRole());
            }
            if (userDetails.getOposicion() != null) {
                existingUser.setOposicion(userDetails.getOposicion());
            }
            if (userDetails.getFotoUrl() != null) {
                existingUser.setFotoUrl(userDetails.getFotoUrl());
            }

            // Campos primitivos siempre se actualizan
            existingUser.setCreditos(userDetails.getCreditos());
            existingUser.setPagado(userDetails.isPagado());
            existingUser.setActive(userDetails.isActive());

            return userRepository.save(existingUser);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el usuario: " + e.getMessage(), e);
        }
    }

    // Elimina físicamente el usuario y todas sus relaciones
    @Override
    @Transactional
    public void deleteUser(Long id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("Usuario con ID " + id + " no encontrado"));

            // Limpiar todas las relaciones para evitar errores de integridad
            user.getPagos().clear();
            user.getSimulacros().clear();
            user.getMarcasOpositor().clear();

            // Guardar cambios primero para limpiar relaciones
            userRepository.save(user);

            // Ahora eliminar el usuario
            userRepository.deleteById(id);

        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Error de integridad al eliminar usuario: " + e.getMessage(), e);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("Usuario con ID " + id + " no encontrado");
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el usuario: " + e.getMessage(), e);
        }
    }

    // Cambia estado activo/inactivo del usuario
    @Override
    @Transactional
    public void toggleUserStatus(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario con ID " + id + " no encontrado"));

        try {
            user.setActive(!user.isActive());
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error al cambiar el estado del usuario: " + e.getMessage(), e);
        }
    }

    // Actualiza solo los créditos del usuario
    @Override
    @Transactional
    public void updateUserCredits(Long id, int newCredits) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario con ID " + id + " no encontrado"));

        try {
            user.setCreditos(newCredits);
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar los créditos del usuario: " + e.getMessage(), e);
        }
    }
}


