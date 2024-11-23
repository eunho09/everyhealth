package com.example.everyhealth.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Data
public class TodayExerciseDto {

    private String exerciseName;
    private List<ArrayList<Integer>> repWeight;
    private Integer sequence;

    public TodayExerciseDto(String exerciseName, List<ArrayList<Integer>> repWeight, Integer sequence) {
        this.exerciseName = exerciseName;
        this.repWeight = repWeight;
        this.sequence = sequence;
    }
}
