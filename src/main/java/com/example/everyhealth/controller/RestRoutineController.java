package com.example.everyhealth.controller;

import com.example.everyhealth.aop.ExtractMemberId;
import com.example.everyhealth.domain.Member;
import com.example.everyhealth.domain.Routine;
import com.example.everyhealth.domain.RoutineExercise;
import com.example.everyhealth.dto.*;
import com.example.everyhealth.security.JwtTokenGenerator;
import com.example.everyhealth.service.MemberService;
import com.example.everyhealth.service.RoutineExerciseService;
import com.example.everyhealth.service.RoutineService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
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
    public ResponseEntity<Long> save(@ExtractMemberId Long memberId,
                                     @RequestBody RoutineSaveDto dto) {

        Member findMember = memberService.findById(memberId);
        Routine routine = new Routine(dto.getName(), findMember);
        routineService.save(routine);

        return ResponseEntity.status(HttpStatus.CREATED).body(routine.getId());
    }

    @PostMapping("/routineExercise")
    public ResponseEntity<String> addExercise(@RequestBody RoutineDto dto) {
        routineService.addExercise(dto.getRoutineId(), dto.getExerciseInfoList());

        return ResponseEntity.ok("RoutineExercise created");
    }


    @GetMapping("/member/routines")
    public List<RoutineResponseDto> findRoutineWithExercises(@ExtractMemberId Long memberId) {
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
    public ResponseEntity<Void> deleteRoutine(@PathVariable Long id) {
        Routine routine = routineService.findById(id);
        routineService.delete(routine);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/routineExercise/{id}")
    public ResponseEntity<Void> deleteRoutineExercise(@PathVariable Long id) {
        RoutineExercise routineExercise = routineExerciseService.findById(id);
        routineExerciseService.delete(routineExercise);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/routineExercise/updateSequence/{routineId}")
    public ResponseEntity<String> updateSequence(@PathVariable Long routineId, @RequestBody List<RoutineExerciseSequence> routineExerciseSequence) {
        routineExerciseService.updateSequence(routineExerciseSequence, routineId);
        return ResponseEntity.ok("update sequence");
    }

    @PatchMapping("/routineExercise/update/{routineId}")
    public ResponseEntity<String> updateRepWeight(@PathVariable Long routineId, @RequestBody List<REResponseDto> responseDtoList) {
        routineExerciseService.updateRepWeight(responseDtoList, routineId);
        return ResponseEntity.ok("update RepWeight");
    }
}
