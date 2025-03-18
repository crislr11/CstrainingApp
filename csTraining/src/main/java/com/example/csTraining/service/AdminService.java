package com.example.csTraining.service;

import com.example.csTraining.entity.User;
import java.util.List;
import java.util.Optional;

public interface AdminService {

    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    User updateUser(Long id, User userDetails);

    void deleteUser(Long id);

    void toggleUserStatus(Long id);

    void updateUserCredits(Long id, int newCredits);
}
