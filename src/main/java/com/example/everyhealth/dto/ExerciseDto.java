package com.example.everyhealth.dto;

import com.example.everyhealth.domain.Exercise;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExerciseDto {

    private Long id;
    private String name;
    private String memo;
    private List<ArrayList<Integer>> repWeight;
    private String classification;

    public ExerciseDto(Exercise exercise) {
        this.id = exercise.getId();
        this.name = exercise.getName();
        this.memo = exercise.getMemo();
        this.repWeight = exercise.getRepWeight();
        this.classification = exercise.getClassification();
    }

    public ExerciseDto() {
    }
}
