package com.example.csTraining.service;

import com.example.csTraining.entity.User;
import java.util.List;

public interface AdminService {

    List<User> getAllUsers();

    User getUserById(Long id);

    User updateUser(Long id, User userDetails);

    void deleteUser(Long id);

    void toggleUserStatus(Long id);

    void updateUserCredits(Long id, int newCredits);

}
