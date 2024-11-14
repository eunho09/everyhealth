package com.example.everyhealth.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class RoutineDto {

    private Long routineId;
    private Map<Long, Integer> exerciseInfo;
}
