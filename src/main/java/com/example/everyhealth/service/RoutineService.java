package com.example.everyhealth.service;

import com.example.everyhealth.domain.Exercise;
import com.example.everyhealth.domain.Routine;
import com.example.everyhealth.domain.RoutineExercise;
import com.example.everyhealth.repository.ExerciseRepository;
import com.example.everyhealth.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoutineService {

    private final RoutineRepository routineRepository;
    private final ExerciseRepository exerciseRepository;

    @Transactional
    public Long save(Routine routine) {
        routineRepository.save(routine);
        return routine.getId();
    }

    public Routine findById(Long id) {
        return routineRepository.findById(id).get();
    }

    public List<Routine> findAll() {
        return routineRepository.findAll();
    }

    @Transactional
    public Routine addExercise(Long routineId, List<Long> exerciseId) {
        Routine routine = findById(routineId);
//        Exercise exercise = exerciseRepository.findById(exerciseId).get();
//        new RoutineExercise(exercise, routine);
        exerciseId.stream()
                .forEach(id -> {
                    Exercise exercise = exerciseRepository.findById(id).get();
                    new RoutineExercise(exercise, routine);
                });


        routineRepository.save(routine);
        return routine;
    }
}
