package com.example.everyhealth.dto;

import com.example.everyhealth.domain.RepWeight;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TodayExerciseDto {

    private Long id;
    private String exerciseName;
    private List<RepWeightDto> repWeightList;
    private Integer sequence;

    public TodayExerciseDto(Long id, String exerciseName, List<RepWeightDto> repWeightList, Integer sequence) {
        this.id = id;
        this.exerciseName = exerciseName;
        this.repWeightList = repWeightList;
        this.sequence = sequence;
    }
}
