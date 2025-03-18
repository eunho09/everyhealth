package com.example.everyhealth.controller;

import com.example.everyhealth.aop.ExtractMemberId;
import com.example.everyhealth.domain.Exercise;
import com.example.everyhealth.domain.Member;
import com.example.everyhealth.domain.RepWeight;
import com.example.everyhealth.dto.ExerciseCreateDto;
import com.example.everyhealth.dto.ExerciseDto;
import com.example.everyhealth.dto.ExerciseResponseDto;
import com.example.everyhealth.security.JwtTokenGenerator;
import com.example.everyhealth.service.ExerciseService;
import com.example.everyhealth.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RestExerciseController {

    private final ExerciseService exerciseService;
    private final MemberService memberService;

    @GetMapping("/exercise/{id}")
    public ResponseEntity<ExerciseResponseDto> findById(@PathVariable Long id) {
        Exercise findExercise = exerciseService.fetchById(id);
        ExerciseResponseDto response = new ExerciseResponseDto(findExercise);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/member/exercises")
    public ResponseEntity<List<ExerciseResponseDto>> findExerciseByMemberId(@ExtractMemberId Long memberId) {
        List<Exercise> exercises = exerciseService.findExercisesByMemberId(memberId);
        List<ExerciseResponseDto> response = exercises.stream()
                .map(ExerciseResponseDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/exercises")
    public ResponseEntity<List<ExerciseResponseDto>> findAll() {
        List<Exercise> exercises = exerciseService.findAll();
        List<ExerciseResponseDto> response = exercises.stream()
                .map(ExerciseResponseDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }


    @PostMapping("/exercise")
    public ResponseEntity<String> save(@ExtractMemberId Long memberId, @RequestBody ExerciseCreateDto dto) {
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
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody ExerciseDto dto) {
        exerciseService.update(id, dto);
        return ResponseEntity.ok("운동을 수정했습니다.");
    }

    @DeleteMapping("/exercise/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        exerciseService.delete(id);
        return ResponseEntity.ok("운동을 삭제했습니다.");
    }
}
