package com.example.everyhealth.controller;

import com.example.everyhealth.aop.ExtractMemberId;
import com.example.everyhealth.domain.RoutineExercise;
import com.example.everyhealth.dto.*;
import com.example.everyhealth.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
@Validated
@Tag(name = "루틴 관리")
public class RestRoutineController {

    private final RoutineDataService routineDataService;
    private final RoutineBusinessService routineBusinessService;
    private final RoutineExerciseService routineExerciseService;
    private final RepWeightService repWeightService;



    @PostMapping("/routine")
    @Operation(summary = "루틴 저장")
    public ResponseEntity<String> save(@ExtractMemberId Long memberId,
                                       @RequestParam @NotBlank(message = "이름을 필수로 입력하세요") String name) {
        String routineName = routineBusinessService.createRoutine(memberId, name);

        return ResponseEntity.status(HttpStatus.CREATED).body(routineName + "을 생성했습니다.");
    }

    @PostMapping("/routineExercise")
    @Operation(summary = "루틴의 운동 추가")
    public ResponseEntity<String> addExercise(@RequestBody RoutineAddExerciseDto dto) {
        routineBusinessService.addExercise(dto.getRoutineId(), dto.getExerciseInfoList());

        return ResponseEntity.ok("RoutineExercise created");
    }


    @GetMapping("/member/routines")
    @Operation(summary = "나의 루틴 조회")
    public ResponseEntity<List<RoutineResponseDto>> memberRoutines(@ExtractMemberId Long memberId) {
        List<RoutineResponseDto> routineResponseDtos = routineBusinessService.fetchRoutinesByMemberId(memberId);

        return ResponseEntity.ok(routineResponseDtos);
    }


    @GetMapping("/routine/{routineId}")
    @Operation(summary = "루틴 조회")
    public ResponseEntity<List<RoutineExerciseResponseDto>> findOne(@PathVariable Long routineId) {
        List<RoutineExerciseResponseDto> routines = routineBusinessService.getRoutine(routineId);
        return ResponseEntity.ok(routines);
    }

    @DeleteMapping("/routine/{id}")
    @Operation(summary = "루틴 삭제")
    public ResponseEntity<Void> deleteRoutine(@PathVariable Long id) {
        routineBusinessService.deleteRoutine(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/routineExercise/{id}")
    @Operation(summary = "루틴의 운동 삭제")
    public ResponseEntity<Void> deleteRoutineExercise(@PathVariable Long id) {
        routineBusinessService.deleteRoutineExercise(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/routineExercise/updateSequence/{routineId}")
    @Operation(summary = "루틴의 운동 순서 업데이트")
    public ResponseEntity<String> updateSequence(@PathVariable Long routineId, @RequestBody List<RoutineExerciseSequence> routineExerciseSequence) {
        routineExerciseService.updateSequence(routineExerciseSequence, routineId);
        return ResponseEntity.ok("update sequence");
    }

    @PatchMapping("/routineExercise/update/{routineId}")
    @Operation(summary = "루틴의 운동 업데이트")
    public ResponseEntity<String> updateRepWeight(@PathVariable Long routineId, @RequestBody List<RoutineExerciseUpdateDto> responseDtoList) {
        routineExerciseService.updateRepWeight(responseDtoList, routineId);
        return ResponseEntity.ok("update RepWeight");
    }

}
