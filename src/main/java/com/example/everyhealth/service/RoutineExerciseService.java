package com.example.everyhealth.service;

import com.example.everyhealth.domain.RoutineExercise;
import com.example.everyhealth.dto.REResponseDto;
import com.example.everyhealth.dto.RoutineExerciseSequence;
import com.example.everyhealth.repository.RoutineExerciseRepository;
import com.example.everyhealth.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RoutineExerciseService {

    private final RoutineExerciseRepository routineExerciseRepository;
    private final RoutineRepository routineRepository;

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

    @Transactional
    public void delete(RoutineExercise routineExercise) {
        routineExerciseRepository.delete(routineExercise);
    }

    public List<RoutineExercise> findRoutineExerciseByRoutineId(Long routineId) {
        return routineExerciseRepository.findRoutineExerciseByRoutineId(routineId);
    }

    public List<RoutineExercise> findRoutineExerciseByMemberId(Long memberId) {
        return routineExerciseRepository.findRoutineExerciseByMemberId(memberId);
    }

    @Transactional
    public String updateSequence(List<RoutineExerciseSequence> routineExerciseSequence, Long routineId) {
        List<RoutineExercise> findRoutineExercise = routineExerciseRepository.findByRoutineId(routineId);
        for (RoutineExercise routineExercise : findRoutineExercise){
            for (RoutineExerciseSequence sequence : routineExerciseSequence) {
                if (routineExercise.getId().equals(sequence.getRoutineExerciseId())) {
                    routineExercise.setSequence(sequence.getSequence());
                }
            }
        }

        return "수정완료";
    }

/*    @Transactional
    public void updateRepWeight(List<REResponseDto> responseDtoList, Long routineId) {
        Routine routine = routineRepository.findById(routineId).get();
        List<RoutineExercise> findRoutineExercise = routineExerciseRepository.findByRoutineId(routineId);
        for (RoutineExercise routineExercise : findRoutineExercise) {
            for (REResponseDto response : responseDtoList) {
                if (routineExercise.getId().equals(response.getRoutineExerciseId())) {
                    routineExercise.setRepWeight(response.getRepWeight());
                }
            }
        }

        routineRepository.save(routine);
    }*/

    @Transactional
    public void updateRepWeight(List<REResponseDto> responseDtoList, Long routineId) {
        List<RoutineExercise> findRoutineExercise = routineExerciseRepository.findByRoutineId(routineId);

        for (RoutineExercise routineExercise : findRoutineExercise) {
            for (REResponseDto response : responseDtoList) {
                if (routineExercise.getId().equals(response.getRoutineExerciseId())) {
                    routineExercise.setRepWeight(response.getRepWeight());
                }
            }
        }
    }

}

