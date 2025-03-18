package com.example.everyhealth.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoutineResponseDto {

    private Long routineId;
    private String routineName;
    private List<RoutineExerciseResponseDto> routineExerciseDtoList;

    public RoutineResponseDto(Long routineId, String routineName, List<RoutineExerciseResponseDto> routineExerciseDtoList) {
        this.routineId = routineId;
        this.routineName = routineName;
        this.routineExerciseDtoList = routineExerciseDtoList;
    }
}
