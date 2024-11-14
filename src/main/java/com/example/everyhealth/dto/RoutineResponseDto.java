package com.example.everyhealth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class RoutineResponseDto {

    private Long routineId;
    private String routineName;
    private List<REResponseDto> routineExerciseDtoList;

    public RoutineResponseDto(Long routineId, String routineName, List<REResponseDto> routineExerciseDtoList) {
        this.routineId = routineId;
        this.routineName = routineName;
        this.routineExerciseDtoList = routineExerciseDtoList;
    }
}
