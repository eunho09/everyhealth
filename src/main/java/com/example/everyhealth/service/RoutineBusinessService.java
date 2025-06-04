package com.example.everyhealth.service;

import com.example.everyhealth.domain.*;
import com.example.everyhealth.dto.ExerciseInfo;
import com.example.everyhealth.dto.RoutineDto;
import com.example.everyhealth.dto.RoutineExerciseResponseDto;
import com.example.everyhealth.dto.RoutineResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoutineBusinessService {

    private final MemberService memberService;
    private final RoutineDataService routineDataService;
    private final RoutineExerciseService routineExerciseService;
    private final RepWeightService repWeightService;
    private final ExerciseDataService exerciseDataService;

    @Transactional
    public String createRoutine(Long memberId, String name) {
        Member findMember = memberService.findById(memberId);
        Routine routine = new Routine(name, findMember);
        routineDataService.save(routine);

        return routine.getName();
    }

    public List<RoutineExerciseResponseDto> getRoutine(Long routineId) {
        List<RoutineExercise> routineExerciseList = routineExerciseService.findAllByRoutineIdWithExerciseAndRepWeight(routineId);
        return routineExerciseList.stream()
                .map(routineExercise -> new RoutineExerciseResponseDto(routineExercise))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteRoutine(Long id) {
        repWeightService.deleteByRoutineId(id);
        routineExerciseService.deleteByRoutineId(id);
        routineDataService.deleteById(id);
    }

    @Transactional
    public void deleteRoutineExercise(Long id) {
        repWeightService.deleteByRoutineExerciseId(id);
        routineExerciseService.deleteById(id);
    }

    @Transactional
    @CachePut(value = "routines", key = "#routineId")
    @CacheEvict(value = {"allRoutines", "routinesByMember"}, allEntries = true)
    public RoutineDto addExercise(Long routineId, List<ExerciseInfo> exerciseInfoList) {
        Routine routine = routineDataService.findById(routineId);
        exerciseInfoList
                .forEach(exercise -> {
                    Exercise findExercise = exerciseDataService.fetchById(exercise.getExerciseId());
                    RoutineExercise routineExercise = new RoutineExercise(findExercise, routine, exercise.getSequence());

                    List<RepWeight> repWeightList = findExercise.getRepWeightList();
                    repWeightList
                            .forEach(rw -> new RepWeight(rw.getReps(), rw.getWeight(), routineExercise));
                });

        routineDataService.save(routine);

        RoutineDto routineDto = new RoutineDto(routine);
        return routineDto;
    }

    @Cacheable(value = "routinesByMember", key = "#memberId")
    public List<RoutineResponseDto> fetchRoutinesByMemberId(Long memberId) {
        List<Routine> routineList = routineDataService.fetchByMemberId(memberId);
        List<RoutineExercise> routineExerciseList = routineExerciseService.fetchByRoutineIds(routineList.stream().map(r -> r.getId()).toList());

        Map<Long, RoutineExercise> routineExerciseMap = routineExerciseList.stream()
                .collect(Collectors.toMap(re -> re.getId(), dto -> dto));

        List<RoutineResponseDto> responseDtoList = routineList.stream()
                .map(r -> new RoutineResponseDto(r, routineExerciseMap))
                .collect(Collectors.toList());

        return responseDtoList;
    }
}
