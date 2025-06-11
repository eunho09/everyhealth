package com.example.everyhealth.dto;

import com.example.everyhealth.domain.RepWeight;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RepWeightDto {

    private Long id;

    private int reps;

    private double weight;

    public RepWeightDto(RepWeight repWeight) {
        this.id = repWeight.getId();
        this.reps = repWeight.getReps();
        this.weight = repWeight.getWeight();
    }

    public RepWeightDto(int reps, double weight) {
        this.reps = reps;
        this.weight = weight;
    }
}
