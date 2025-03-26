package com.example.everyhealth.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoutineDto {

    private Long id;
    private String name;
    private List<RoutineExerciseDto> routineExercises;
    private MemberDto member;

    public RoutineDto(Long id, String name, List<RoutineExerciseDto> routineExercises, MemberDto member) {
        this.id = id;
        this.name = name;
        this.routineExercises = routineExercises;
        this.member = member;
    }
}
