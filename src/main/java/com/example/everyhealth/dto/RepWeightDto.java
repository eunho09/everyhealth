package com.example.everyhealth.dto;

import com.example.everyhealth.domain.RepWeight;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "반복횟수/무게 DTO")
public class RepWeightDto {

    @Schema(description = "반복횟수/무게 ID", example = "1")
    private Long id;
    @Schema(description = "반복횟수", example = "20")
    private int reps;
    @Schema(description = "무게", example = "10")
    private double weight;

    public RepWeightDto(RepWeight repWeight) {
        this.id = repWeight.getId();
        this.reps = repWeight.getReps();
        this.weight = repWeight.getWeight();
    }
}
