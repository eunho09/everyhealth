package com.example.everyhealth.dto;

import com.example.everyhealth.domain.Classification;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ExerciseCreateDto {

    @NotBlank
    private String name;

    private String memo;

    @NotNull
    @NotEmpty
    private List<RepWeightDto> repWeightList;

    @NotNull
    private Classification classification;
}
