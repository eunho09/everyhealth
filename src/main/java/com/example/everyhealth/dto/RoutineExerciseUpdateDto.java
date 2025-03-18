package com.example.everyhealth.dto;

import com.example.everyhealth.domain.RepWeight;
import lombok.Data;

import java.util.List;

@Data
public class RoutineExerciseUpdateDto {

    private Long routineExerciseId;
    private Integer sequence;
    private List<RepWeight> repWeightList;
    private String exerciseName;

    public RoutineExerciseUpdateDto(Long routineExerciseId, Integer sequence, List<RepWeight> repWeightList, String exerciseName) {
        this.routineExerciseId = routineExerciseId;
        this.sequence = sequence;
        this.repWeightList = repWeightList;
        this.exerciseName = exerciseName;
    }
}
