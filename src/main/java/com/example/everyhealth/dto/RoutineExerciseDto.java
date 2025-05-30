package com.example.everyhealth.dto;

import com.example.everyhealth.domain.RepWeight;
import com.example.everyhealth.domain.RoutineExercise;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class RoutineExerciseDto {

    private Long id;
    private Integer sequence;
    private List<RepWeightDto> repWeightList;
    private String exerciseName;

    public RoutineExerciseDto(RoutineExercise routineExercise) {
        this.id = routineExercise.getId();
        this.sequence = routineExercise.getSequence();
        this.repWeightList = DtoConverter.convertRepWeights(routineExercise.getRepWeightList());
        this.exerciseName = routineExercise.getExercise().getName();
    }
}
