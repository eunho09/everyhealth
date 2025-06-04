package com.example.everyhealth.controller;

import com.example.everyhealth.aop.ExtractMemberId;
import com.example.everyhealth.domain.ExerciseUpdateDto;
import com.example.everyhealth.dto.ExerciseCreateDto;
import com.example.everyhealth.dto.ExerciseResponseDto;
import com.example.everyhealth.service.ExerciseBusinessService;
import com.example.everyhealth.service.ExerciseDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "운동 관리")
public class RestExerciseController {

    private final ExerciseDataService exerciseDataService;
    private final ExerciseBusinessService exerciseBusinessService;

    @GetMapping("/exercise/{id}")
    @Operation(summary = "운동 1개 조회")
    public ResponseEntity<ExerciseResponseDto> fetchById(@PathVariable Long id) {
        ExerciseResponseDto exerciseResponseDto = exerciseBusinessService.fetchOne(id);
        return ResponseEntity.ok(exerciseResponseDto);
    }


    @GetMapping("/member/exercises")
    @Operation(summary = "나의 모든 운동 조회")
    public ResponseEntity<List<ExerciseResponseDto>> findExerciseByMemberId(@ExtractMemberId Long memberId) {
        List<ExerciseResponseDto> exerciseResponseDtos = exerciseBusinessService.fetchMemberExercises(memberId);
        return ResponseEntity.ok(exerciseResponseDtos);
    }

    @GetMapping("/exercises")
    @Operation(summary = "모든 운동 조회")
    public ResponseEntity<List<ExerciseResponseDto>> findAll() {
        List<ExerciseResponseDto> exerciseResponseDtos = exerciseBusinessService.fetchAll();
        return ResponseEntity.ok(exerciseResponseDtos);
    }


    @PostMapping("/exercise")
    @Operation(summary = "운동 저장")
    public ResponseEntity<String> save(@ExtractMemberId Long memberId,
                                       @Valid @RequestBody ExerciseCreateDto dto,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());

            return ResponseEntity.badRequest().body(errors.toString());
        }

        String exerciseName = exerciseBusinessService.createExercise(memberId, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(exerciseName + "을 저장했습니다.");
    }

    @PatchMapping("/exercise/{id}")
    @Operation(summary = "운동 업데이트")
    public ResponseEntity<String> update(@PathVariable Long id,
                                         @Valid @RequestBody ExerciseUpdateDto dto,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());

            return ResponseEntity.badRequest().body(errors.toString());
        }
        exerciseBusinessService.update(id, dto);
        return ResponseEntity.ok("운동을 수정했습니다.");
    }

    @DeleteMapping("/exercise/{id}")
    @Operation(summary = "운동 삭제")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        exerciseBusinessService.delete(id);
        return ResponseEntity.ok("운동을 삭제했습니다.");
    }
}
