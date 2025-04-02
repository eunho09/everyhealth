package com.example.everyhealth.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ExerciseUpdateDto {
    private Long id;

    @NotBlank
    private String name;
    private String memo;

    @NotNull
    @NotEmpty
    private List<RepWeight> repWeightList;

    @NotNull
    private Classification classification;

    public ExerciseUpdateDto(Exercise exercise) {
        this.id = exercise.getId();
        this.name = exercise.getName();
        this.memo = exercise.getMemo();
        this.repWeightList = exercise.getRepWeightList();
        this.classification = exercise.getClassification();
    }
}
