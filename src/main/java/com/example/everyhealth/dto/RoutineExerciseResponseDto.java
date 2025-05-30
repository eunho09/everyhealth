package com.example.everyhealth.dto;

import com.example.everyhealth.domain.RoutineExercise;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class RoutineExerciseResponseDto {

    private Long routineExerciseId;
    private Integer sequence;
    private List<RepWeightDto> repWeightList;
    private String exerciseName;

    public RoutineExerciseResponseDto(RoutineExercise routineExercise) {
        this.routineExerciseId = routineExercise.getId();
        this.sequence = routineExercise.getSequence();
        this.repWeightList = DtoConverter.convertRepWeights(routineExercise.getRepWeightList());
        this.exerciseName = routineExercise.getExercise().getName();
    }
}
