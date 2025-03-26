package com.example.everyhealth.dto;

import com.example.everyhealth.domain.CheckBox;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class TodayDateDto {

    private LocalDate localDate;
    private CheckBox checkBox;

    public TodayDateDto(LocalDate localDate, CheckBox checkbox) {
        this.localDate = localDate;
        this.checkBox = checkbox;
    }
}
