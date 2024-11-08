package com.example.everyhealth.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoutineDto {

    private Long routineId;
    private List<Long> exerciseIdArr;
}
