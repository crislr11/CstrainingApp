package com.example.csTraining.service.impl;

import com.example.csTraining.entity.User;
import com.example.csTraining.exceptions.UserNotFoundException;
import com.example.csTraining.repository.UserRepository;
import com.example.csTraining.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service("adminService")
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error al recuperar los usuarios: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<User> getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user;
        } else {
            throw new UserNotFoundException("Usuario con ID " + id + " no encontrado");
        }
    }



    @Override
    @Transactional
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario con ID " + id + " no encontrado"));

        try {
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());
            user.setActive(userDetails.isActive());
            user.setOposion(userDetails.getOposion());
            user.setCreditos(userDetails.getCreditos());
            user.setPagado(userDetails.isPagado());

            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteUser(Long id) {
        try {
            if (!userRepository.existsById(id)) {
                throw new UserNotFoundException("No se puede eliminar: Usuario con ID " + id + " no encontrado");
            }
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("No se puede eliminar: Usuario con ID " + id + " no encontrado");
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el usuario: " + e.getMessage(), e);
        }
    }

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
