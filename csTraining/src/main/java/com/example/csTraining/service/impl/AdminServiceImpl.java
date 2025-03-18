package com.example.csTraining.service.impl;

import com.example.csTraining.entity.User;
import com.example.csTraining.exceptions.UserNotFoundException;
import com.example.csTraining.repository.UserRepository;
import com.example.csTraining.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("adminService")
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario con ID " + id + " no encontrado")));
    }

    @Override
    public User updateUser(Long id, User userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());
            user.setActive(userDetails.isActive());
            user.setOposion(userDetails.getOposion());
            user.setCreditos(userDetails.getCreditos());
            user.setPagado(userDetails.isPagado());
            return userRepository.save(user);
        }).orElseThrow(() -> new UserNotFoundException("Usuario con ID " + id + " no encontrado"));
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("No se puede eliminar: Usuario con ID " + id + " no encontrado");
        }
        userRepository.deleteById(id);
    }

    @Override
    public void toggleUserStatus(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario con ID " + id + " no encontrado"));
        user.setActive(!user.isActive());
        userRepository.save(user);
    }

    @Override
    public void updateUserCredits(Long id, int newCredits) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario con ID " + id + " no encontrado"));

        user.setCreditos(newCredits);
        userRepository.save(user);
    }

}
