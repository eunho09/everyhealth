package com.example.everyhealth.dto;

import com.example.everyhealth.domain.RepWeight;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RoutineExerciseDto {

    private Long id;
    private Integer sequence;
    private List<RepWeightDto> repWeightList;
    private String exerciseName;

    public RoutineExerciseDto(Long id, Integer sequence, List<RepWeightDto> repWeightList, String exerciseName) {
        this.id = id;
        this.sequence = sequence;
        this.repWeightList = repWeightList;
        this.exerciseName = exerciseName;
    }
}
