package com.example.everyhealth.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class RoutineExercise {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id")
    private Routine routine;

    private Integer sequence;

    @ElementCollection
    @CollectionTable(
            name = "routine_exercise_rep_weight",
            joinColumns = @JoinColumn(name = "routine_exercise_id") // RoutineExercise와 연결
    )
    private List<ArrayList<Integer>> repWeight = new ArrayList<>();

    public RoutineExercise(Exercise exercise, Routine routine, Integer sequence, List<ArrayList<Integer>> repWeight) {
        this.exercise = exercise;
        exercise.getRoutineExerciseList().add(this);
        this.routine = routine;
        routine.getRoutineExerciseList().add(this);
        this.sequence = sequence;
        this.repWeight = new ArrayList<>(repWeight);
    }

    public RoutineExercise() {
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
        exercise.getRoutineExerciseList().add(this);
    }

    public void setRoutine(Routine routine) {
        this.routine = routine;
        routine.getRoutineExerciseList().add(this);
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public void setRepWeight(List<ArrayList<Integer>> repWeight) {
        this.repWeight = repWeight;
    }
}
