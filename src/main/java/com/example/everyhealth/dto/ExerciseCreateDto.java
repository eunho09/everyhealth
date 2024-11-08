package com.example.everyhealth.dto;

import com.example.everyhealth.domain.Member;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExerciseCreateDto {
    private String name;
    private Member member;
    private String memo;
    private List<Integer> repetitions;
    private List<Integer> weight;
    private String classification;
}
