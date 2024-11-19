package com.example.everyhealth.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class REResponseDto {

    private Long routineExerciseId;
    private Integer sequence;
    private List<ArrayList<Integer>> repWeight;
    private String exerciseName;

    public REResponseDto(Long routineExerciseId,Integer sequence, List<ArrayList<Integer>> repWeight, String exerciseName) {
        this.routineExerciseId = routineExerciseId;
        this.sequence = sequence;
        this.repWeight = repWeight;
        this.exerciseName = exerciseName;
    }
}
