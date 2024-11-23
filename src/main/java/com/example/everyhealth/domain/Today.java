package com.example.everyhealth.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Today {

    @Id @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "today", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TodayExercise> todayExercises = new ArrayList<>(); //운동기록 n개

    private LocalDate localDate;

    @Enumerated(EnumType.STRING)
    private CheckBox checkBox;

    private int recodeTime;

    public Today(LocalDate localDate) {
        this.localDate = localDate;
        this.checkBox = CheckBox.False;
    }

    public Today() {
    }
}
