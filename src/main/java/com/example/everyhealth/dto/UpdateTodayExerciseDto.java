package com.example.everyhealth.dto;

import com.example.everyhealth.domain.RepWeight;
import lombok.Data;

import java.util.List;

@Data
public class UpdateTodayExerciseDto {

    private Long id;
    private List<RepWeight> repWeightList;
}
