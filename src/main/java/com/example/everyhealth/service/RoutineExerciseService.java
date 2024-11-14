package com.example.everyhealth.service;

import com.example.everyhealth.domain.RoutineExercise;
import com.example.everyhealth.dto.RoutineExerciseDto;
import com.example.everyhealth.repository.RoutineExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoutineExerciseService {

    private final RoutineExerciseRepository routineExerciseRepository;

    @Transactional
    public Long save(RoutineExercise routineExercise) {
        routineExerciseRepository.save(routineExercise);
        return routineExercise.getId();
    }

    public RoutineExercise findById(Long id) {
        return routineExerciseRepository.findById(id).get();
    }

    public List<RoutineExercise> findAll() {
        return routineExerciseRepository.findAll();
    }

    public List<RoutineExercise> findRoutineExerciseByRoutineId(Long routineId) {
        return routineExerciseRepository.findRoutineExerciseByRoutineId(routineId);
    }

    public List<RoutineExercise> findRoutineExerciseByMemberId(Long memberId) {
        return routineExerciseRepository.findRoutineExerciseByMemberId(memberId);
    }
}

