package com.example.csTraining.service;

import com.example.csTraining.controller.models.AuthResponse;
import com.example.csTraining.controller.models.AuthenticationRequest;
import com.example.csTraining.controller.models.RegisterRequest;


public interface AuthService {

    public AuthResponse authenticate(AuthenticationRequest authenticationRequest);

    public AuthResponse register(RegisterRequest request);

}
