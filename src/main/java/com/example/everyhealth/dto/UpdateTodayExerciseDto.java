package com.example.everyhealth.dto;

import com.example.everyhealth.domain.RepWeight;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UpdateTodayExerciseDto {

    private Long id;
    private List<RepWeightDto> repWeightList;
}
