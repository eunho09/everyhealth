package com.example.everyhealth.dto;

import com.example.everyhealth.domain.RepWeight;
import com.example.everyhealth.domain.Routine;
import com.example.everyhealth.domain.RoutineExercise;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class RoutineResponseDto {

    private Long routineId;
    private String routineName;
    private List<RoutineExerciseResponseDto> routineExerciseDtoList;

    public RoutineResponseDto(Routine routine, Map<Long, RoutineExercise> routineExerciseMap) {
        this.routineId = routine.getId();
        this.routineName = routine.getName();
        this.routineExerciseDtoList = routine.getRoutineExerciseList().stream()
                .map(re -> {
                    RoutineExercise routineExercise = routineExerciseMap.get(re.getId());
                    return new RoutineExerciseResponseDto(routineExercise);
                })
                .collect(Collectors.toList());
    }
}
