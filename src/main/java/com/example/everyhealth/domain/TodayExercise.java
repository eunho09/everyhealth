package com.example.everyhealth.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class TodayExercise {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "today_id")
    private Today today;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @OneToMany(mappedBy = "todayExercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RepWeight> repWeightList = new ArrayList<>();

    private Integer sequence;

    public TodayExercise(Exercise exercise, Today today, Integer sequence) {
        this.exercise = exercise;
        exercise.getTodayExercises().add(this);
        this.today = today;
        today.getTodayExercises().add(this);
        this.sequence = sequence;
    }

    public TodayExercise() {
    }


    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }
}
