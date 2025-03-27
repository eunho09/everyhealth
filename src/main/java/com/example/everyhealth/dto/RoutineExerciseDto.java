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
        this.repWeightList = routineExercise.getRepWeightList().stream()
                .map(rw -> new RepWeightDto(rw))
                .collect(Collectors.toList());
        this.exerciseName = routineExercise.getExercise().getName();
    }
}
