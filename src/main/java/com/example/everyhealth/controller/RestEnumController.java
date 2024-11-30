package com.example.everyhealth.controller;

import com.example.everyhealth.domain.Classification;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RestEnumController {

    @GetMapping("/enum/classification")
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
