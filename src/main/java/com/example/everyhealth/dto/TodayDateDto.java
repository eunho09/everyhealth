package com.example.everyhealth.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TodayDateDto {

    private LocalDate localDate;

    public TodayDateDto(LocalDate localDate) {
        this.localDate = localDate;
    }
}
