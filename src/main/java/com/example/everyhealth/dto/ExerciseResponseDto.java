package com.example.everyhealth.dto;

import com.example.everyhealth.domain.Classification;
import com.example.everyhealth.domain.Exercise;
import com.example.everyhealth.domain.RepWeight;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ExerciseResponseDto {
    private Long id;
    private String name;
    private String memo;
    private List<RepWeightDto> repWeightList;
    private Classification classification;

    public ExerciseResponseDto(Exercise exercise) {
        this.id = exercise.getId();
        this.name = exercise.getName();
        this.memo = exercise.getMemo();
        this.repWeightList = exercise.getRepWeightList().stream()
                .map(rw -> new RepWeightDto(rw))
                .collect(Collectors.toList());
        this.classification = exercise.getClassification();
    }
}
