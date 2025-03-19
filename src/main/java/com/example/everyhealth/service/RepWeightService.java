package com.example.everyhealth.service;

import com.example.everyhealth.repository.RepWeightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RepWeightService {

    private final RepWeightRepository repWeightRepository;

    @Transactional
    public void deleteByRoutineId(Long routineId) {
        repWeightRepository.deleteByRoutineId(routineId);
    }

    @Transactional
    public void deleteByRoutineExerciseId(Long routineExerciseId) {
        repWeightRepository.deleteByRoutineExerciseId(routineExerciseId);
    }

    @Transactional
    public void deleteByExerciseId(Long exerciseId) {
        repWeightRepository.deleteByExerciseId(exerciseId);
    }
}
