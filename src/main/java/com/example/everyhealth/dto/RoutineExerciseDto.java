package com.example.everyhealth.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RoutineExerciseDto {

    private Integer sequence;
    private String routineName;
    private List<ArrayList<Integer>> repWeight;
    private String exerciseName;

    public RoutineExerciseDto(Integer sequence, String routineName, List<ArrayList<Integer>> repWeight, String exerciseName) {
        this.sequence = sequence;
        this.routineName = routineName;
        this.repWeight = repWeight;
        this.exerciseName = exerciseName;
    }
}
