package com.example.csTraining.controller;

import com.example.csTraining.entity.Entrenamiento;
import com.example.csTraining.entity.Oposicion;
import com.example.csTraining.entity.User;
import com.example.csTraining.exceptions.EntrenamientoNotFoundException;
import com.example.csTraining.service.EntrenamientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/entrenamientos")
@RequiredArgsConstructor
public class EntrenamientoController {

    @Autowired
    @Qualifier("entrenamientoService")
    private EntrenamientoService entrenamientoService;



}
