package com.example.everyhealth.domain;

import jakarta.persistence.*;
import lombok.Getter;

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

    public RoutineExercise(Exercise exercise, Routine routine) {
        this.exercise = exercise;
        exercise.getRoutineExerciseList().add(this);
        this.routine = routine;
        routine.getRoutineExerciseList().add(this);
    }

    public RoutineExercise() {
    }

    public static RoutineExercise createRoutineExercise(Routine routine, Exercise exercise) {
        RoutineExercise routineExercise = new RoutineExercise();
        routineExercise.setRoutine(routine);
        routineExercise.setExercise(exercise);

        return routineExercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public void setRoutine(Routine routine) {
        this.routine = routine;
    }
}
