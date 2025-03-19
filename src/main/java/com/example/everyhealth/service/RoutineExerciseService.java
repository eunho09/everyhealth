package com.example.everyhealth.service;

import com.example.everyhealth.domain.RepWeight;
import com.example.everyhealth.domain.RoutineExercise;
import com.example.everyhealth.dto.RoutineExerciseSequence;
import com.example.everyhealth.dto.RoutineExerciseUpdateDto;
import com.example.everyhealth.repository.RoutineExerciseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
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

    @Transactional
    public void delete(RoutineExercise routineExercise) {
        routineExerciseRepository.delete(routineExercise);
    }

    public List<RoutineExercise> fetchRoutineExerciseByRoutineId(Long routineId) {
        return routineExerciseRepository.fetchRoutineExerciseByRoutineId(routineId);
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

    @Transactional
    public void updateRepWeight(List<RoutineExerciseUpdateDto> responseDtoList, Long routineId) {
        List<RoutineExercise> findRoutineExercise = routineExerciseRepository.findByRoutineId(routineId);

        Map<Long, RoutineExerciseUpdateDto> responseDtoMap = responseDtoList.stream()
                .collect(Collectors.toMap(RoutineExerciseUpdateDto::getRoutineExerciseId, dto -> dto));

        for (RoutineExercise routineExercise : findRoutineExercise) {
            Long routineExerciseId = routineExercise.getId();

            if (responseDtoMap.containsKey(routineExerciseId)) {
                RoutineExerciseUpdateDto responseDto = responseDtoMap.get(routineExerciseId);
                List<RepWeight> newRepWeightList = responseDto.getRepWeightList();

                List<RepWeight> existRepWeight = routineExercise.getRepWeightList();

                Set<Long> newRepWeightIds = newRepWeightList.stream()
                        .map(RepWeight::getId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet());

                existRepWeight.removeIf(rw -> !newRepWeightIds.contains(rw.getId()));

                for (RepWeight newRepWeight : newRepWeightList) {
                    if (newRepWeight.getId() == null) {
                        new RepWeight(
                                newRepWeight.getReps(),
                                newRepWeight.getWeight(),
                                routineExercise);
                    } else{
                        existRepWeight.stream()
                                .filter(rw -> rw.getId().equals(newRepWeight.getId()))
                                .findFirst()
                                .ifPresent(rw -> {
                                    rw.setReps(newRepWeight.getReps());
                                    rw.setWeight(newRepWeight.getWeight());
                                });
                    }
                }
            }
        }
    }

    public List<RoutineExercise> findAllByRoutineIdWithExerciseAndRepWeight(Long routineId) {
        return routineExerciseRepository.findAllByRoutineIdWithExerciseAndRepWeight(routineId);
    }

    @Transactional
    public void deleteById(Long routineExerciseId) {
        routineExerciseRepository.deleteById(routineExerciseId);
    }

    @Transactional
    public void deleteByRoutineId(Long routineId) {
        routineExerciseRepository.deleteByRoutineId(routineId);
    }

    public List<RoutineExercise> fetchByRoutineIds(List<Long> routineIds) {
        return routineExerciseRepository.fetchByRoutineIds(routineIds);
    }
}

