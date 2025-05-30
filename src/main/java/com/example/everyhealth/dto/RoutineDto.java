package com.example.everyhealth.dto;

import com.example.everyhealth.domain.Routine;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class RoutineDto {

    private Long id;
    private String name;
    private List<RoutineExerciseDto> routineExercises;
    private MemberDto member;

    public RoutineDto(Routine routine) {
        this.id = routine.getId();
        this.name = routine.getName();
        this.routineExercises = DtoConverter.convertList(routine.getRoutineExerciseList(), RoutineExerciseDto::new);
        this.member = new MemberDto(routine.getMember());
    }
}
