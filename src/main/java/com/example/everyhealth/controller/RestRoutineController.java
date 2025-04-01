package com.example.everyhealth.controller;

import com.example.everyhealth.aop.ExtractMemberId;
import com.example.everyhealth.domain.Member;
import com.example.everyhealth.domain.Routine;
import com.example.everyhealth.domain.RoutineExercise;
import com.example.everyhealth.dto.*;
import com.example.everyhealth.service.MemberService;
import com.example.everyhealth.service.RepWeightService;
import com.example.everyhealth.service.RoutineExerciseService;
import com.example.everyhealth.service.RoutineService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
@Validated
public class RestRoutineController {

    private final RoutineService routineService;
    private final MemberService memberService;
    private final RoutineExerciseService routineExerciseService;
    private final RepWeightService repWeightService;



    @PostMapping("/routine")
    public ResponseEntity<String> save(@ExtractMemberId Long memberId,
                                       @RequestParam @NotBlank(message = "이름을 필수로 입력하세요") String name) {
        Member findMember = memberService.findById(memberId);
        Routine routine = new Routine(name, findMember);
        routineService.save(routine);

        return ResponseEntity.status(HttpStatus.CREATED).body(routine.getName() + "을 생성했습니다.");
    }

    @PostMapping("/routineExercise")
    public ResponseEntity<String> addExercise(@RequestBody RoutineAddExerciseDto dto) {
        routineService.addExercise(dto.getRoutineId(), dto.getExerciseInfoList());

        return ResponseEntity.ok("RoutineExercise created");
    }


    @GetMapping("/member/routines")
    public ResponseEntity<List<RoutineResponseDto>> memberRoutines(@ExtractMemberId Long memberId) {
        List<RoutineResponseDto> routineResponseDtos = routineService.fetchRoutinesByMemberId(memberId);

        return ResponseEntity.ok(routineResponseDtos);
    }


    @GetMapping("/routine/{routineId}")
    public ResponseEntity<List<RoutineExerciseResponseDto>> findOne(@PathVariable Long routineId) {

        List<RoutineExercise> routineExerciseList = routineExerciseService.findAllByRoutineIdWithExerciseAndRepWeight(routineId);
        List<RoutineExerciseResponseDto> responseList = routineExerciseList.stream()
                .map(routineExercise -> new RoutineExerciseResponseDto(routineExercise))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @DeleteMapping("/routine/{id}")
    public ResponseEntity<Void> deleteRoutine(@PathVariable Long id) {
        repWeightService.deleteByRoutineId(id);
        routineExerciseService.deleteByRoutineId(id);
        routineService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/routineExercise/{id}")
    public ResponseEntity<Void> deleteRoutineExercise(@PathVariable Long id) {
        repWeightService.deleteByRoutineExerciseId(id);
        routineExerciseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/routineExercise/updateSequence/{routineId}")
    public ResponseEntity<String> updateSequence(@PathVariable Long routineId, @RequestBody List<RoutineExerciseSequence> routineExerciseSequence) {
        routineExerciseService.updateSequence(routineExerciseSequence, routineId);
        return ResponseEntity.ok("update sequence");
    }

    @PatchMapping("/routineExercise/update/{routineId}")
    public ResponseEntity<String> updateRepWeight(@PathVariable Long routineId, @RequestBody List<RoutineExerciseUpdateDto> responseDtoList) {
        routineExerciseService.updateRepWeight(responseDtoList, routineId);
        return ResponseEntity.ok("update RepWeight");
    }

}
