package com.example.everyhealth.dto;

import lombok.Data;

import java.util.List;

@Data
public class TodayExerciseRequest {

    private Long id;
    private String type;
    private Integer sequence;
}
