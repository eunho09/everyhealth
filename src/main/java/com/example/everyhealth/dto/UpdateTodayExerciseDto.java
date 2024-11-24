package com.example.everyhealth.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UpdateTodayExerciseDto {

    private Long id;
    private List<ArrayList<Integer>> repWeight;
}
