package com.example.everyhealth.controller;

import com.example.everyhealth.domain.Routine;
import com.example.everyhealth.dto.RoutineDto;
import com.example.everyhealth.service.RoutineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RestRoutineController {

    private final RoutineService routineService;

    @PostMapping("/routine")
    public String save(@RequestBody String name) {
        Routine routine = new Routine(name);
        routineService.save(routine);

        return "루틴 저장 완료";
    }

    @PostMapping("/routineExercise")
    public String addExercise(@RequestBody RoutineDto dto) {
        routineService.addExercise(dto.getRoutineId(), dto.getExerciseIdArr());

        return "루틴 운동 저장 완료";
    }
}
