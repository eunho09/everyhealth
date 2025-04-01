package com.example.everyhealth.controller;

import com.example.everyhealth.aop.ExtractMemberId;
import com.example.everyhealth.domain.Exercise;
import com.example.everyhealth.domain.ExerciseUpdateDto;
import com.example.everyhealth.domain.Member;
import com.example.everyhealth.domain.RepWeight;
import com.example.everyhealth.dto.ExerciseCreateDto;
import com.example.everyhealth.dto.ExerciseDto;
import com.example.everyhealth.dto.ExerciseResponseDto;
import com.example.everyhealth.security.JwtTokenGenerator;
import com.example.everyhealth.service.ExerciseService;
import com.example.everyhealth.service.MemberService;
import com.example.everyhealth.service.RepWeightService;
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
public class RestExerciseController {

    private final ExerciseService exerciseService;
    private final MemberService memberService;
    private final RepWeightService repWeightService;

    @GetMapping("/exercise/{id}")
    public ResponseEntity<ExerciseResponseDto> fetchById(@PathVariable Long id) {
        ExerciseResponseDto exerciseResponseDto = exerciseService.fetchOne(id);
        return ResponseEntity.ok(exerciseResponseDto);
    }


    @GetMapping("/member/exercises")
    public ResponseEntity<List<ExerciseResponseDto>> findExerciseByMemberId(@ExtractMemberId Long memberId) {
        List<ExerciseResponseDto> exerciseResponseDtos = exerciseService.fetchMemberExercises(memberId);
        return ResponseEntity.ok(exerciseResponseDtos);
    }

    @GetMapping("/exercises")
    public ResponseEntity<List<ExerciseResponseDto>> findAll() {
        List<ExerciseResponseDto> exerciseResponseDtos = exerciseService.fetchAll();
        return ResponseEntity.ok(exerciseResponseDtos);
    }


    @PostMapping("/exercise")
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

        List<RepWeight> repWeightList = dto.getRepWeightList();
        repWeightList
                .forEach(r -> r.setExercise(exercise));

        exerciseService.save(exercise);

        return ResponseEntity.status(HttpStatus.CREATED).body(exercise.getName() + "을 저장했습니다.");
    }

    @PatchMapping("/exercise/{id}")
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
    public ResponseEntity<String> delete(@PathVariable Long id) {
        repWeightService.deleteByExerciseId(id);
        exerciseService.deleteById(id);
        return ResponseEntity.ok("운동을 삭제했습니다.");
    }
}
