package com.example.everyhealth.dto;

import com.example.everyhealth.domain.Classification;
import com.example.everyhealth.domain.Member;
import com.example.everyhealth.domain.RepWeight;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExerciseCreateDto {

    @NotBlank
    private String name;
    private String memo;

    @NotNull
    @NotEmpty
    private List<RepWeight> repWeightList;

    @NotNull
    private Classification classification;
}
