package com.example.everyhealth.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoutineAddExerciseDto {

    private Long routineId;
    private List<ExerciseInfo> exerciseInfoList;
}
