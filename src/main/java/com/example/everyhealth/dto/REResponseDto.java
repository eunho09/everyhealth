package com.example.everyhealth.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class REResponseDto {

    private Integer sequence;
    private List<ArrayList<Integer>> repWeight;
    private String exerciseName;

    public REResponseDto(Integer sequence, List<ArrayList<Integer>> repWeight, String exerciseName) {
        this.sequence = sequence;
        this.repWeight = repWeight;
        this.exerciseName = exerciseName;
    }
}
