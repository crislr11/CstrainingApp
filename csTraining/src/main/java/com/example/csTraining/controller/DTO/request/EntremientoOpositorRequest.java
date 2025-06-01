package com.example.csTraining.controller.DTO.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntremientoOpositorRequest {

    private Long entrenamientoId;
    private Long userId;

}
