package com.example.everyhealth.controller;

import com.example.everyhealth.domain.Today;
import com.example.everyhealth.domain.TodayExercise;
import com.example.everyhealth.dto.*;
import com.example.everyhealth.service.TodayExerciseService;
import com.example.everyhealth.service.TodayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RestTodayController {

    private final TodayService todayService;
    private final TodayExerciseService todayExerciseService;

    @PostMapping("/today")
    public ResponseEntity<String> save(@RequestBody TodaySaveDto dto) {
        Today today = new Today(dto.getLocalDate());
        todayService.save(today);

        return ResponseEntity.ok("save today");
    }

    @PostMapping("/today/addTodayExercise/exercise")
    public ResponseEntity<String> addTodayExerciseAsExercise(@RequestBody TodayExerciseAsExerciseRequest dto) {
        todayService.addTodayExerciseAsExercise(dto);

        return ResponseEntity.ok("add todayExercise");
    }

    @PostMapping("/today/addTodayExercise/{date}")
    public ResponseEntity<String> addTodayExercise(@RequestBody List<TodayExerciseRequest> dto, @PathVariable LocalDate date) {
        todayService.addTodayExercise(dto, date);

        return ResponseEntity.ok("add todayExercise");
    }

    @GetMapping("/today/{todayId}")
    public ResponseEntity<TodayDto> findById(@PathVariable Long todayId) {

        Today today = todayService.findById(todayId);
        List<TodayExercise> todayExerciseList = todayExerciseService.findByTodayId(todayId);

        List<TodayExerciseDto> todayExerciseDtoList = todayExerciseList.stream()
                .map(todayExercise -> new TodayExerciseDto(
                        todayExercise.getId(),
                        todayExercise.getExercise().getName(),
                        todayExercise.getRepWeight(),
                        todayExercise.getSequence()
                ))
                .collect(Collectors.toList());

        TodayDto dto = new TodayDto(
                today.getId(),
                todayExerciseDtoList,
                today.getLocalDate(),
                today.getCheckBox()
        );

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/today/month/{month}")
    public ResponseEntity<List<TodayDateDto>> findByMonth(@PathVariable int month) {
        List<TodayDateDto> todayList = todayService.findByMonth(month);
        return ResponseEntity.ok(todayList);
    }

    @GetMapping("/today/date/{date}")
    public ResponseEntity<TodayDto> fetchByLocalDate(@PathVariable LocalDate date) {
        TodayDto todayDto = todayService.fetchByLocalDate(date);
        return ResponseEntity.ok(todayDto);
    }

    @PatchMapping("/update/todayExercise/{todayId}")
    public ResponseEntity<String> updateTodayExercise(@RequestBody List<UpdateTodayExerciseDto> dto, @PathVariable Long todayId) {
        todayService.updateTodayExercise(dto, todayId);
        return ResponseEntity.ok("update TodayExercise");
    }

    @DeleteMapping("/delete/todayExercise/{id}")
    public ResponseEntity<String> deleteTodayExercise(@PathVariable Long id) {
        TodayExercise todayExercise = todayExerciseService.findById(id);
        todayExerciseService.delete(todayExercise);
        return ResponseEntity.ok("delete todayExercise");
    }

    @PatchMapping("/todayExercise/updateSequence/{todayId}")
    public ResponseEntity<String> updateSequence(@RequestBody List<UpdateSeqTodayExercise> todayExerciseList, @PathVariable Long todayId) {
        todayService.updateSequence(todayExerciseList, todayId);

        return ResponseEntity.ok("update sequence todayExercise");
    }
}
