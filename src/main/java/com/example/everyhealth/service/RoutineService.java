package com.example.everyhealth.service;

import com.example.everyhealth.domain.Exercise;
import com.example.everyhealth.domain.RepWeight;
import com.example.everyhealth.domain.Routine;
import com.example.everyhealth.domain.RoutineExercise;
import com.example.everyhealth.dto.*;
import com.example.everyhealth.repository.ExerciseRepository;
import com.example.everyhealth.repository.RoutineExerciseRepository;
import com.example.everyhealth.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RoutineService {

    private final RoutineRepository routineRepository;
    private final ExerciseRepository exerciseRepository;
    private final RoutineExerciseRepository routineExerciseRepository;

    @Transactional
    @CachePut(value = "routines" , key = "#routine.id")
    @CacheEvict(value = {"allRoutines", "routinesByMember"}, allEntries = true)
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
    @CacheEvict(value = "routines", key = "#routine.id")
    public void delete(Routine routine) {
        routineRepository.delete(routine);
    }

    @Transactional
    @CachePut(value = "routines", key = "#routineId")
    @CacheEvict(value = {"allRoutines", "routinesByMember"}, allEntries = true)
    public RoutineDto addExercise(Long routineId, List<ExerciseInfo> exerciseInfoList) {
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

        RoutineDto routineDto = new RoutineDto(
                routine.getId(),
                routine.getName(),
                routine.getRoutineExerciseList().stream()
                        .map(re -> new RoutineExerciseDto(
                                re.getId(),
                                re.getSequence(),
                                re.getRepWeightList().stream()
                                        .map(rw -> new RepWeightDto(
                                                rw.getId(),
                                                rw.getReps(),
                                                rw.getWeight()
                                        ))
                                        .toList(),
                                re.getExercise().getName()
                        ))
                        .toList(),
                new MemberDto(
                        routine.getMember().getId(),
                        routine.getMember().getName(),
                        routine.getMember().getPicture()
                )
        );

        return routineDto;
    }

    @Cacheable(value = "routinesByMember", key = "#memberId")
    public List<RoutineResponseDto> fetchRoutinesByMemberId(Long memberId) {
        List<Routine> routineList = routineRepository.fetchByMemberId(memberId);
        List<RoutineExercise> routineExerciseList = routineExerciseRepository.fetchByRoutineIds(routineList.stream().map(r -> r.getId()).toList());

        Map<Long, RoutineExercise> routineExerciseMap = routineExerciseList.stream()
                .collect(Collectors.toMap(re -> re.getId(), dto -> dto));

        List<RoutineResponseDto> responseDtoList = routineList.stream()
                .map(r -> new RoutineResponseDto(
                        r.getId(),
                        r.getName(),
                        r.getRoutineExerciseList().stream()
                                .map(re -> {
                                    RoutineExercise fetchRe = routineExerciseMap.get(re.getId());
                                    return new RoutineExerciseResponseDto(
                                            fetchRe.getId(),
                                            fetchRe.getSequence(),
                                            fetchRe.getRepWeightList().stream()
                                                    .map(rw -> new RepWeightDto(
                                                            rw.getId(),
                                                            rw.getReps(),
                                                            rw.getWeight()
                                                    ))
                                                    .collect(Collectors.toList()),
                                            fetchRe.getExercise().getName()
                                    );
                                }).collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        return responseDtoList;
    }

    @Transactional
    @CacheEvict(value = {"routines", "allRoutines", "routinesByMember"}, allEntries = true)
    public void deleteById(Long routineId) {
        routineRepository.deleteById(routineId);
    }

}
