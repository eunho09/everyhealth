package com.example.everyhealth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TodayExerciseRequest {

    private Long id;
    private String type;
    private Integer sequence;
}
