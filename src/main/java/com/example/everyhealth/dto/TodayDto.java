package com.example.everyhealth.dto;

import com.example.everyhealth.domain.CheckBox;
import com.example.everyhealth.domain.Today;
import com.example.everyhealth.domain.TodayExercise;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class TodayDto {

    private Long id;
    private List<TodayExerciseDto> todayExercises;
    private LocalDate localDate;
    private CheckBox checkBox;

    public TodayDto(Today today, Map<Long, TodayExercise> todayExercise) {
        this.id = today.getId();
        this.todayExercises = today.getTodayExercises().stream()
                .map(te -> {
                    TodayExercise fetchTe = todayExercise.get(te.getId());
                    return new TodayExerciseDto(fetchTe);
                })
                .collect(Collectors.toList());
        this.localDate = today.getLocalDate();
        this.checkBox = today.getCheckBox();
    }

    public TodayDto(Today today, List<TodayExercise> todayExercise) {
        this.id = today.getId();
        this.todayExercises = todayExercise.stream()
                .map(te -> new TodayExerciseDto(te))
                .collect(Collectors.toList());
        this.localDate = today.getLocalDate();
        this.checkBox = today.getCheckBox();
    }
}
