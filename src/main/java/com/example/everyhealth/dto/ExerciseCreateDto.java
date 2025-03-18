package com.example.everyhealth.dto;

import com.example.everyhealth.domain.Classification;
import com.example.everyhealth.domain.Member;
import com.example.everyhealth.domain.RepWeight;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExerciseCreateDto {
    private String name;
    private String memo;
    private List<RepWeight> repWeightList;
    private Classification classification;
}
