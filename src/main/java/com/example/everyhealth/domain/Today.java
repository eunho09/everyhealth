package com.example.everyhealth.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Today {

    @Id @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "today")
    private List<TodayExercise> todayExercises = new ArrayList<>(); //운동기록 n개

    private LocalDateTime localDateTime;

    private String status;
}
