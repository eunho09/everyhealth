package com.example.everyhealth.controller;

import com.example.everyhealth.aop.ExtractMemberId;
import com.example.everyhealth.domain.Exercise;
import com.example.everyhealth.domain.ExerciseUpdateDto;
import com.example.everyhealth.domain.Member;
import com.example.everyhealth.domain.RepWeight;
import com.example.everyhealth.dto.DtoConverter;
import com.example.everyhealth.dto.ExerciseCreateDto;
import com.example.everyhealth.dto.ExerciseResponseDto;
import com.example.everyhealth.dto.RepWeightDto;
import com.example.everyhealth.service.ExerciseService;
import com.example.everyhealth.service.MemberService;
import com.example.everyhealth.service.RepWeightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "운동 관리")
public class RestExerciseController {

    private final ExerciseService exerciseService;
    private final MemberService memberService;
    private final RepWeightService repWeightService;

    @GetMapping("/exercise/{id}")
    @Operation(summary = "운동 1개 조회")
    public ResponseEntity<ExerciseResponseDto> fetchById(@PathVariable Long id) {
        ExerciseResponseDto exerciseResponseDto = exerciseService.fetchOne(id);
        return ResponseEntity.ok(exerciseResponseDto);
    }


    @GetMapping("/member/exercises")
    @Operation(summary = "나의 모든 운동 조회")
    public ResponseEntity<List<ExerciseResponseDto>> findExerciseByMemberId(@ExtractMemberId Long memberId) {
        List<ExerciseResponseDto> exerciseResponseDtos = exerciseService.fetchMemberExercises(memberId);
        return ResponseEntity.ok(exerciseResponseDtos);
    }

    @GetMapping("/exercises")
    @Operation(summary = "모든 운동 조회")
    public ResponseEntity<List<ExerciseResponseDto>> findAll() {
        List<ExerciseResponseDto> exerciseResponseDtos = exerciseService.fetchAll();
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
        Member findMember = memberService.findById(memberId);

        Exercise exercise = new Exercise(dto.getName(),
                findMember,
                dto.getMemo(),
                dto.getClassification());

        List<RepWeightDto> repWeightList = dto.getRepWeightList();
        repWeightList.forEach(r -> new RepWeight(r.getReps(), r.getWeight(), exercise));

        exerciseService.save(exercise);

        return ResponseEntity.status(HttpStatus.CREATED).body(exercise.getName() + "을 저장했습니다.");
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
        exerciseService.update(id, dto);
        return ResponseEntity.ok("운동을 수정했습니다.");
    }

    @DeleteMapping("/exercise/{id}")
    @Operation(summary = "운동 삭제")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        repWeightService.deleteByExerciseId(id);
        exerciseService.deleteById(id);
        return ResponseEntity.ok("운동을 삭제했습니다.");
    }
}
