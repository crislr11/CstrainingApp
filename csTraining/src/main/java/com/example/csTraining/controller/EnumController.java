package com.example.csTraining.controller;

import com.example.csTraining.entity.Role;
import com.example.csTraining.entity.Oposicion;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class EnumController {

    // Devuelve los nombres de los roles
    @GetMapping("/api/roles")
    public List<String> getRoles() {
        return Arrays.stream(Role.values())
                .map(Enum::name)
                .toList();
    }

    // Devuelve los nombres de las oposiciones
    @GetMapping("/api/oposiciones")
    public List<String> getOposiciones() {
        return Arrays.stream(Oposicion.values())
                .map(Enum::name)
                .toList();
    }
}
