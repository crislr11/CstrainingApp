// AdminController.java
package com.example.csTraining.controller;

import com.example.csTraining.entity.User;
import com.example.csTraining.exceptions.UserNotFoundException;
import com.example.csTraining.exceptions.UserAlreadyExistsException;
import com.example.csTraining.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/user")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // Lista todos los usuarios
    @PreAuthorize("hasAnyRole('ADMIN','PROFESOR')")
    @GetMapping("/listar")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = adminService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Busca usuario por ID
    @PreAuthorize("hasAnyRole('ADMIN','PROFESOR')")
    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(adminService.getUserById(id));
        } catch (UserNotFoundException ex) {
            return handleUserNotFoundException(ex);
        }
    }

    // Actualiza usuario preservando información existente
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        try {
            User updatedUser = adminService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (UserNotFoundException ex) {
            return handleUserNotFoundException(ex);
        } catch (Exception ex) {
            return handleGenericException(ex);
        }
    }

    // Elimina físicamente el usuario y todos sus datos
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            adminService.deleteUser(id);
            return ResponseEntity.ok("Usuario eliminado completamente");
        } catch (UserNotFoundException ex) {
            return handleUserNotFoundException(ex);
        } catch (Exception ex) {
            return handleGenericException(ex);
        }
    }

    // Cambia estado activo/inactivo
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/cambiarEstado")
    public ResponseEntity<?> toggleUserStatus(@PathVariable Long id) {
        try {
            adminService.toggleUserStatus(id);
            return ResponseEntity.ok("Estado del usuario cambiado");
        } catch (UserNotFoundException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    // Actualiza créditos del usuario
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/actualizarCreditos")
    public ResponseEntity<?> updateUserCredits(@PathVariable Long id, @RequestBody int newCredits) {
        try {
            adminService.updateUserCredits(id, newCredits);
            return ResponseEntity.ok("Créditos del usuario actualizados");
        } catch (UserNotFoundException ex) {
            return handleUserNotFoundException(ex);
        } catch (Exception ex) {
            return handleGenericException(ex);
        }
    }

    // --------- MANEJO DE ERRORES ---------

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + ex.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error inesperado: " + ex.getMessage());
    }
}