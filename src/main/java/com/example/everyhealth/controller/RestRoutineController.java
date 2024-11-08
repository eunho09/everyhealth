package com.example.everyhealth.controller;

import com.example.everyhealth.service.RoutineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RestRoutineController {

    private final RoutineService routineService;
}
