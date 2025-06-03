package com.example.everyhealth.controller;

import com.example.everyhealth.domain.Classification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "열거형 관리")
public class RestEnumController {

    @GetMapping("/enum/classification")
    @Operation(summary = "운동의 타겟 부위 목록")
    public Map<String, Classification> getClassification() {
        return Map.of(
                "삼두", Classification.TRICEPS,
                "이두", Classification.BICEPS,
                "가슴", Classification.CHEST,
                "등", Classification.BACK,
                "어깨", Classification.SHOULDER,
                "복근", Classification.ABS,
                "하체", Classification.LOWERBODY
        );
    }
}
