package com.example.everyhealth.controller;

import com.example.everyhealth.domain.Exercise;
import com.example.everyhealth.domain.Member;
import com.example.everyhealth.dto.ExerciseCreateDto;
import com.example.everyhealth.dto.ExerciseDto;
import com.example.everyhealth.service.ExerciseService;
import com.example.everyhealth.service.MemberService;
import lombok.RequiredArgsConstructor;
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
    public ExerciseDto findById(@PathVariable Long id) {
        Exercise findExercise = exerciseService.findById(id);
        return new ExerciseDto(findExercise);
    }

    @GetMapping("/exercises")
    public List<ExerciseDto> findAll() {
        List<Exercise> exercises = exerciseService.findAll();
        return exercises.stream()
                .map(e -> new ExerciseDto(e))
                .collect(Collectors.toList());
    }

    @PostMapping("/exercise")
    public String save(@RequestBody ExerciseCreateDto dto) {
        Member member = dto.getMember();
        Member findMember = memberService.findById(member.getId());

        Exercise exercise = new Exercise(dto.getName(),
                findMember,
                dto.getMemo(),
                dto.getRepetitions(),
                dto.getWeight(),
                dto.getClassification());

        exerciseService.save(exercise);

        return "운동 저장 완료";
    }

    //실험 필요
    @PatchMapping("/exercise/{id}")
    public String update(@PathVariable Long id, @RequestBody ExerciseDto dto) {
        exerciseService.update(id, dto);
        return "운동 수정 완료";
    }

    @DeleteMapping("/exercise/{id}")
    public String delete(@PathVariable Long id) {
        exerciseService.delete(id);
        return "운동 삭제 완료";
    }
}
