package com.example.everyhealth.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoutineExerciseResponseDto {

    private Long routineExerciseId;
    private Integer sequence;
    private List<RepWeightDto> repWeightList;
    private String exerciseName;

    public RoutineExerciseResponseDto(Long routineExerciseId, Integer sequence, List<RepWeightDto> repWeightList, String exerciseName) {
        this.routineExerciseId = routineExerciseId;
        this.sequence = sequence;
        this.repWeightList = repWeightList;
        this.exerciseName = exerciseName;
    }
}
