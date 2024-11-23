package com.example.everyhealth.service;

import com.example.everyhealth.domain.Exercise;
import com.example.everyhealth.domain.Routine;
import com.example.everyhealth.domain.RoutineExercise;
import com.example.everyhealth.dto.ExerciseInfo;
import com.example.everyhealth.repository.ExerciseRepository;
import com.example.everyhealth.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
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
    public Routine addExercise(Long routineId, List<ExerciseInfo> exerciseInfoList) {
        Routine routine = findById(routineId);
        exerciseInfoList.stream()
                .forEach(exercise -> {
                    Exercise findExercise = exerciseRepository.fetchById(exercise.getExerciseId());
                    new RoutineExercise(findExercise, routine, exercise.getSequence(), findExercise.getRepWeight());
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

    public Routine findByIdOrderBySequence(Long routineId) {
        return routineRepository.findByIdOrOrderBySequence(routineId);
    }

}
