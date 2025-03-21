package com.example.everyhealth.dto;

import com.example.everyhealth.domain.Classification;
import com.example.everyhealth.domain.Exercise;
import com.example.everyhealth.domain.RepWeight;
import lombok.Data;

import java.util.List;

@Data
public class ExerciseDto {

    private Long id;
    private String name;
    private String memo;
    private List<RepWeight> repWeightList;
    private Classification classification;

    public ExerciseDto(Exercise exercise) {
        this.id = exercise.getId();
        this.name = exercise.getName();
        this.memo = exercise.getMemo();
        this.repWeightList = exercise.getRepWeightList();
        this.classification = exercise.getClassification();
    }

    public ExerciseDto() {
    }
}
