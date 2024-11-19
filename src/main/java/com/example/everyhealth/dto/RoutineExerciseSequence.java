package com.example.everyhealth.dto;

import lombok.Data;

@Data
public class RoutineExerciseSequence {
    private Long routineExerciseId;
    private Integer sequence;


    public RoutineExerciseSequence(Long routineExerciseId, Integer sequence) {
        this.routineExerciseId = routineExerciseId;
        this.sequence = sequence;
    }
}
