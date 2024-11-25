package com.example.everyhealth.controller;

import com.example.everyhealth.domain.Exercise;
import com.example.everyhealth.domain.Member;
import com.example.everyhealth.dto.ExerciseCreateDto;
import com.example.everyhealth.dto.ExerciseDto;
import com.example.everyhealth.service.ExerciseService;
import com.example.everyhealth.service.MemberService;
import lombok.Data;
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

    //멤버 전용도 만들어야 함
    private final ExerciseService exerciseService;
    private final MemberService memberService;

    @GetMapping("/exercise/{id}")
    public ExerciseDto findById(@PathVariable Long id) {
        Exercise findExercise = exerciseService.fetchById(id);
        return new ExerciseDto(findExercise);
    }


    @GetMapping("/member/{memberId}/exercises")
    public List<ExerciseDto> findExerciseByMemberId(@PathVariable Long memberId) {
        List<Exercise> exercises = exerciseService.findExercisesByMemberId(memberId);
        return exercises.stream()
                .map(e -> new ExerciseDto(e))
                .collect(Collectors.toList());
    }

    @GetMapping("/exercises")
    public List<ExerciseDto> findAll() {
        List<Exercise> exercises = exerciseService.findAll();
        return exercises.stream()
                .map(e -> new ExerciseDto(e))
                .collect(Collectors.toList());
    }

    @PostMapping("/exercise")
    public ResponseEntity<Long> save(@RequestBody ExerciseCreateDto dto) {
        Member findMember = memberService.findById(dto.getMemberId());

        Exercise exercise = new Exercise(dto.getName(),
                findMember,
                dto.getMemo(),
                dto.getRepWeight(),
                dto.getClassification());

        exerciseService.save(exercise);

        return ResponseEntity.status(HttpStatus.CREATED).body(exercise.getId());
    }

    //실험 필요
    @PatchMapping("/exercise/{id}")
    public String update(@PathVariable Long id, @RequestBody ExerciseDto dto) {
        exerciseService.update(id, dto);
        return "운동 수정 완료";
    }

    @DeleteMapping("/exercise/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        exerciseService.delete(id);
        return ResponseEntity.ok("delete exercise");
    }
}
