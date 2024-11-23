package com.example.everyhealth.dto;

import lombok.Data;

import java.util.List;

@Data
public class TodayExerciseAsExerciseRequest {

    private Long todayId;
    private List<ExerciseInfo> exerciseInfoList;

    public TodayExerciseAsExerciseRequest(Long todayId, List<ExerciseInfo> exerciseInfoList) {
        this.todayId = todayId;
        this.exerciseInfoList = exerciseInfoList;
    }
}
