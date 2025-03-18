package com.example.everyhealth.service;

import com.example.everyhealth.domain.Exercise;
import com.example.everyhealth.domain.RepWeight;
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
        exerciseInfoList
                .forEach(exercise -> {
                    Exercise findExercise = exerciseRepository.fetchById(exercise.getExerciseId());
                    RoutineExercise routineExercise = new RoutineExercise(findExercise, routine, exercise.getSequence());

                    List<RepWeight> repWeightList = findExercise.getRepWeightList();
                    repWeightList
                            .forEach(rw -> new RepWeight(rw.getReps(), rw.getWeight(), routineExercise));
                });

        routineRepository.save(routine);
        return routine;
    }

    public List<Routine> findRoutineWithExercises(Long memberId) {
        return routineRepository.findRoutineWithExercises(memberId);
    }

}
