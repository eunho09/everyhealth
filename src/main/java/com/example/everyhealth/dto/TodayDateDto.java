package com.example.everyhealth.dto;

import com.example.everyhealth.domain.CheckBox;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TodayDateDto {

    private LocalDate localDate;
    private CheckBox checkBox;

    public TodayDateDto(LocalDate localDate, CheckBox checkbox) {
        this.localDate = localDate;
        this.checkBox = checkbox;
    }
}
