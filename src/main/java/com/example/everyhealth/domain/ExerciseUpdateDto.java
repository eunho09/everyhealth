package com.example.everyhealth.domain;

import com.example.everyhealth.dto.DtoConverter;
import com.example.everyhealth.dto.RepWeightDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ExerciseUpdateDto {
    private Long id;

    @NotBlank
    private String name;
    private String memo;

    @NotNull
    @NotEmpty
    private List<RepWeightDto> repWeightList;

    @NotNull
    private Classification classification;


    public void setRepWeightList(List<RepWeight> repWeightList) {
        this.repWeightList= DtoConverter.convertRepWeights(repWeightList);
    }

    public void setRepWeightListDto(List<RepWeightDto> repWeightList) {
        this.repWeightList = repWeightList;
    }
}
