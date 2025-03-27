package com.example.everyhealth.dto;

import com.example.everyhealth.domain.RepWeight;
import com.example.everyhealth.domain.TodayExercise;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class TodayExerciseDto {

    private Long id;
    private String exerciseName;
    private List<RepWeightDto> repWeightList;
    private Integer sequence;

    public TodayExerciseDto(TodayExercise todayExercise) {
        this.id = todayExercise.getId();
        this.exerciseName = todayExercise.getExercise().getName();
        this.repWeightList = todayExercise.getRepWeightList().stream()
                .map(rw -> new RepWeightDto(rw))
                .collect(Collectors.toList());
        this.sequence = todayExercise.getSequence();
    }
}
