package com.example.everyhealth.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class RepWeight {

    @Id @GeneratedValue
    private Long id;

    private int reps;
    private double weight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routineExercise_id")
    private RoutineExercise routineExercise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todayExercise_id")
    private TodayExercise todayExercise;

    public RepWeight(int reps, double weight, Exercise exercise) {
        this.reps = reps;
        this.weight = weight;
        setExercise(exercise);
    }

    public RepWeight(int reps, double weight, RoutineExercise routineExercise) {
        this.reps = reps;
        this.weight = weight;
        setRoutineExercise(routineExercise);
    }

    public RepWeight(int reps, double weight, TodayExercise todayExercise) {
        this.reps = reps;
        this.weight = weight;
        setTodayExercise(todayExercise);
    }

    protected RepWeight() {
    }

    public void setExercise(Exercise exercise){
        this.exercise = exercise;
        exercise.getRepWeightList().add(this);
    }

    public void setRoutineExercise(RoutineExercise routineExercise) {
        this.routineExercise = routineExercise;
        routineExercise.getRepWeightList().add(this);
    }

    public void setTodayExercise(TodayExercise todayExercise) {
        this.todayExercise = todayExercise;
        todayExercise.getRepWeightList().add(this);
    }
}
