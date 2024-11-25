package com.example.everyhealth.controller;

import com.example.everyhealth.domain.Member;
import com.example.everyhealth.domain.Routine;
import com.example.everyhealth.domain.RoutineExercise;
import com.example.everyhealth.dto.*;
import com.example.everyhealth.service.MemberService;
import com.example.everyhealth.service.RoutineExerciseService;
import com.example.everyhealth.service.RoutineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class RestRoutineController {

    private final RoutineService routineService;
    private final MemberService memberService;
    private final RoutineExerciseService routineExerciseService;

    @PostMapping("/routine")
    public ResponseEntity<Long> save(@RequestBody RoutineSaveDto dto) {
        Member findMember = memberService.findById(dto.getMemberId());
        Routine routine = new Routine(dto.getName(), findMember);
        routineService.save(routine);

        return ResponseEntity.status(HttpStatus.CREATED).body(routine.getId());
    }

    @PostMapping("/routineExercise")
    public ResponseEntity<String> addExercise(@RequestBody RoutineDto dto) {
        routineService.addExercise(dto.getRoutineId(), dto.getExerciseInfoList());

        return ResponseEntity.ok("RoutineExercise created");
    }

    @GetMapping("/member/{memberId}/routines")
    public List<RoutineResponseDto> findRoutineWithExercises(@PathVariable Long memberId) {
        List<Routine> routineList = routineService.findRoutineWithExercises(memberId);
        List<RoutineExercise> routineExerciseList = routineExerciseService.findAllByRoutineIdWithExerciseAndRepWeight(memberId);

        Map<Long, List<RoutineExercise>> routineExerciseMap = routineExerciseList.stream()
                .collect(Collectors.groupingBy(routineExercise -> routineExercise.getRoutine().getId()));

        return routineList.stream()
                .map(routine -> new RoutineResponseDto(
                        routine.getId(),
                        routine.getName(),
                        routineExerciseMap.getOrDefault(routine.getId(), new ArrayList<>()).stream()
                                .map(routineExercise -> new REResponseDto(
                                        routineExercise.getId(),
                                        routineExercise.getSequence(),
                                        routineExercise.getRepWeight(),
                                        routineExercise.getExercise().getName()
                                ))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }


    @GetMapping("/routine/{routineId}")
    public List<REResponseDto> findOne(@PathVariable Long routineId) {

        List<RoutineExercise> routineExerciseList = routineExerciseService.findAllByRoutineIdWithExerciseAndRepWeight(routineId);
        return routineExerciseList.stream()
                .map(routineExercise -> new REResponseDto(
                        routineExercise.getId(),
                        routineExercise.getSequence(),
                        routineExercise.getRepWeight(),
                        routineExercise.getExercise().getName()
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/routineExercise/{routineId}")
    public List<RoutineExerciseDto> findRoutineExerciseByRoutineId(@PathVariable Long routineId) {
        return routineExerciseService.findRoutineExerciseByRoutineId(routineId)
                .stream()
                .map(routineExercise -> new RoutineExerciseDto(routineExercise.getSequence(), routineExercise.getRoutine().getName(), routineExercise.getExercise().getRepWeight(), routineExercise.getExercise().getName()))
                .collect(Collectors.toList());
    }

    @DeleteMapping("/routine/{id}")
    public ResponseEntity<String> deleteRoutine(@PathVariable Long id) {
        Routine routine = routineService.findById(id);
        routineService.delete(routine);
        return ResponseEntity.ok("delete routine");
    }

    @DeleteMapping("/routineExercise/{id}")
    public ResponseEntity<String> deleteRoutineExercise(@PathVariable Long id) {
        RoutineExercise routineExercise = routineExerciseService.findById(id);
        routineExerciseService.delete(routineExercise);
        return ResponseEntity.ok("delete routineExercise");
    }

    @PutMapping("/routineExercise/updateSequence")
    public ResponseEntity<String> updateSequence(@RequestBody List<RoutineExerciseSequence> routineExerciseSequence, Long routineId) {
        routineExerciseService.updateSequence(routineExerciseSequence, routineId);
        return ResponseEntity.ok("update sequence");
    }

    @PatchMapping("/routineExercise/update")
    public ResponseEntity<String> updateRepWeight(@RequestBody List<REResponseDto> responseDtoList, Long routineId) {
        routineExerciseService.updateRepWeight(responseDtoList, routineId);
        return ResponseEntity.ok("update RepWeight");
    }
}
