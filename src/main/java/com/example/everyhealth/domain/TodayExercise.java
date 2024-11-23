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
    @CollectionTable(
            name = "today_exercise_rep_weight",
            joinColumns = @JoinColumn(name = "today_exercise_id") // RoutineExercise와 연결
    )
    private List<ArrayList<Integer>> repWeight = new ArrayList<>();

    private Integer sequence;

    public TodayExercise(Exercise exercise, Today today, List<ArrayList<Integer>> repWeight, Integer sequence) {
        this.exercise = exercise;
        exercise.getTodayExercises().add(this);
        this.today = today;
        today.getTodayExercises().add(this);
        this.repWeight = new ArrayList<>(repWeight);
        this.sequence = sequence;
    }

    public TodayExercise() {
    }

    public void setRepWeight(List<ArrayList<Integer>> repWeight) {
        this.repWeight = repWeight;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }
}
