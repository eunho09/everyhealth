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

    @ElementCollection
    private List<Integer> repetitions = new ArrayList<>();  // 각 세트의 반복 횟수 저장

    @ElementCollection
    private List<Integer> weights = new ArrayList<>();  // 각 세트의 무게 저장

    public TodayExercise(Exercise exercise, Today today) {
        this.exercise = exercise;
        exercise.getTodayExercises().add(this);
        this.today = today;
        today.getTodayExercises().add(this);
        this.repetitions = exercise.getRepetitions();
        this.weights = exercise.getWeights();
    }

    public TodayExercise() {
    }

    public void setRepetitions(List<Integer> repetitions) {
        this.repetitions = repetitions;
    }

    public void setWeights(List<Integer> weights) {
        this.weights = weights;
    }
}
