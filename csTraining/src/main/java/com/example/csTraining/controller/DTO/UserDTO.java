package com.example.csTraining.controller.DTO;

import com.example.csTraining.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String nombreUsuario;
    private String email;
    private Role role;

}
