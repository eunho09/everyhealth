package com.example.everyhealth.controller;

import com.example.everyhealth.domain.Member;
import com.example.everyhealth.domain.Routine;
import com.example.everyhealth.domain.RoutineExercise;
import com.example.everyhealth.dto.*;
import com.example.everyhealth.service.MemberService;
import com.example.everyhealth.service.RoutineExerciseService;
import com.example.everyhealth.service.RoutineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RestRoutineController {

    private final RoutineService routineService;
    private final MemberService memberService;
    private final RoutineExerciseService routineExerciseService;

    @PostMapping("/routine")
    public String save(@RequestBody RoutineSaveDto dto) {
        Member findMember = memberService.findById(dto.getMemberId());
        Routine routine = new Routine(dto.getName(), findMember);
        routineService.save(routine);

        return "루틴 저장 완료";
    }

    @PostMapping("/routineExercise")
    public String addExercise(@RequestBody RoutineDto dto) {
        routineService.addExercise(dto.getRoutineId(), dto.getExerciseInfo());

        return "루틴 운동 저장 완료";
    }

    @GetMapping("/routine/{memberId}")
    public List<RoutineResponseDto> findRoutineWithExercises(@PathVariable Long memberId) {
        List<Routine> routineList = routineService.findRoutineWithExercises(memberId);
        return routineList.stream()
                .map((routine) -> new RoutineResponseDto(
                        routine.getId(),
                        routine.getName(),
                        routine.getRoutineExerciseList().stream()
                                .map(re -> new REResponseDto(re.getSequence(),
                                        re.getExercise().getRepWeight(),
                                        re.getExercise().getName()))
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    @GetMapping("/routineExercise/{routineId}")
    public List<RoutineExerciseDto> findRoutineExerciseByRoutineId(@PathVariable Long routineId) {
        return routineExerciseService.findRoutineExerciseByRoutineId(routineId)
                .stream()
                .map(routineExercise -> new RoutineExerciseDto(routineExercise.getSequence(), routineExercise.getRoutine().getName(), routineExercise.getExercise().getRepWeight(), routineExercise.getExercise().getName()))
                .collect(Collectors.toList());
    }

    @GetMapping("/routineExercise")
    public List<RoutineExerciseDto> findRoutineExerciseByMemberId(@RequestBody Long memberId) {
        return routineExerciseService.findRoutineExerciseByMemberId(memberId)
                .stream()
                .map(routineExercise -> new RoutineExerciseDto(routineExercise.getSequence(), routineExercise.getRoutine().getName(), routineExercise.getExercise().getRepWeight(), routineExercise.getExercise().getName()))
                .collect(Collectors.toList());
    }
}
