package com.example.everyhealth.dto;

import lombok.Data;

@Data
public class RepWeightDto {

    private Long id;
    private int reps;
    private double weight;

    public RepWeightDto(Long id, int reps, double weight) {
        this.id = id;
        this.reps = reps;
        this.weight = weight;
    }
}
