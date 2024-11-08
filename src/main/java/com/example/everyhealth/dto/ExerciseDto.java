package com.example.everyhealth.dto;

import com.example.everyhealth.domain.Exercise;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExerciseDto {

    private String name;
    private String memo;
    private List<Integer> repetitions;
    private List<Integer> weight;
    private String classification;

    public ExerciseDto(Exercise exercise) {
        this.name = exercise.getName();
        this.memo = exercise.getMemo();
        this.repetitions = exercise.getRepetitions();
        this.weight = exercise.getWeights();
        this.classification = exercise.getClassification();
    }
}
