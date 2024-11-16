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
import java.util.Map;

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
    public void delete(Routine routine) {
        routineRepository.delete(routine);
    }

    @Transactional
    public Routine addExercise(Long routineId, Map<Long, Integer> exerciseInfo) {
        Routine routine = findById(routineId);
        exerciseInfo.forEach((key, value) -> {
            Exercise exercise = exerciseRepository.findById(key).get();
            new RoutineExercise(exercise, routine, value);
        });

        routineRepository.save(routine);
        return routine;
    }

    public List<Routine> findRoutineWithExercises(Long memberId) {
        return routineRepository.findRoutineWithExercises(memberId);
    }

    @Transactional
    public Routine changeSequence(Long memberId, Long routineId, Map<Long, Integer> routineExerciseInfo) {
        Routine routineWithRoutineExercises = routineRepository.findRoutineWithRoutineExercises(memberId, routineId);
        List<RoutineExercise> routineExerciseList = routineWithRoutineExercises.getRoutineExerciseList();

        routineExerciseList.forEach(rx -> {
            if (routineExerciseInfo.containsKey(rx.getId())){
                Integer newSequence = routineExerciseInfo.get(rx.getId());
                rx.setSequence(newSequence);
            }
        });

        return routineWithRoutineExercises;
    }
}
