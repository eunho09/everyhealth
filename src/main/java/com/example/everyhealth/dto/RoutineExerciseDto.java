package com.example.everyhealth.dto;

import com.example.everyhealth.domain.RepWeight;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RoutineExerciseDto {

    private Integer sequence;
    private String routineName;
    private List<RepWeightDto> repWeight;
    private String exerciseName;

    public RoutineExerciseDto(Integer sequence, String routineName, List<RepWeightDto> repWeight, String exerciseName) {
        this.sequence = sequence;
        this.routineName = routineName;
        this.repWeight = repWeight;
        this.exerciseName = exerciseName;
    }
}
