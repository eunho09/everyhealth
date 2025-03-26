package com.example.everyhealth.dto;

import com.example.everyhealth.domain.CheckBox;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class TodayDto {

    private Long id;
    private List<TodayExerciseDto> todayExercises;
    private LocalDate localDate;
    private CheckBox checkBox;

    public TodayDto(Long id, List<TodayExerciseDto> todayExercises, LocalDate localDate, CheckBox checkBox) {
        this.id = id;
        this.todayExercises = todayExercises;
        this.localDate = localDate;
        this.checkBox = checkBox;
    }

    public TodayDto(LocalDate localDate) {
        this.localDate = localDate;
    }
}
