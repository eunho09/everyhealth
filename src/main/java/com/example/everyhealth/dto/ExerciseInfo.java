package com.example.everyhealth.dto;

import lombok.Data;

@Data
public class ExerciseInfo {
    private Long exerciseId;
    private Integer sequence;

    public ExerciseInfo(Long exerciseId, Integer sequence) {
        this.exerciseId = exerciseId;
        this.sequence = sequence;
    }
}
