package com.example.everyhealth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SaveComment {

    private Long postId;

    @NotBlank
    private String text;
}
