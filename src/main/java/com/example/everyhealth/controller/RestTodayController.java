package com.example.everyhealth.controller;

import com.example.everyhealth.domain.Member;
import com.example.everyhealth.domain.Today;
import com.example.everyhealth.domain.TodayExercise;
import com.example.everyhealth.dto.*;
import com.example.everyhealth.security.JwtTokenGenerator;
import com.example.everyhealth.service.MemberService;
import com.example.everyhealth.service.TodayExerciseService;
import com.example.everyhealth.service.TodayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RestTodayController {

    private final JwtTokenGenerator jwtTokenGenerator;
    private final TodayService todayService;
    private final TodayExerciseService todayExerciseService;
    private final MemberService memberService;

    @PostMapping("/today")
    public ResponseEntity<Void> save(@CookieValue(name = "jwt") String token, @RequestBody TodaySaveDto dto) {
        Long memberId = jwtTokenGenerator.getUserId(token);
        Member member = memberService.findById(memberId);
        Today today = new Today(dto.getLocalDate(), member);
        todayService.save(today);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

/*    @PostMapping("/today/addTodayExercise/exercise")
    public ResponseEntity<String> addTodayExerciseAsExercise(@RequestBody TodayExerciseAsExerciseRequest dto) {
        todayService.addTodayExerciseAsExercise(dto);

        return ResponseEntity.ok("add todayExercise");
    }*/

    @PostMapping("/today/addTodayExercise/{date}")
    public ResponseEntity<String> addTodayExercise(@CookieValue(name = "jwt") String token ,@RequestBody List<TodayExerciseRequest> dto, @PathVariable LocalDate date) {
        Long memberId = jwtTokenGenerator.getUserId(token);
        todayService.addTodayExercise(dto, date, memberId);

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
    public ResponseEntity<List<TodayDateDto>> findByMonth(@CookieValue(name = "jwt") String token, @PathVariable int month) {
        Long memberId = jwtTokenGenerator.getUserId(token);
        List<TodayDateDto> todayList = todayService.findByMonth(month, memberId);
        return ResponseEntity.ok(todayList);
    }

    @GetMapping("/today/date/{date}")
    public ResponseEntity<TodayDto> fetchByLocalDate(@CookieValue(name = "jwt") String token, @PathVariable LocalDate date) {
        Long memberId = jwtTokenGenerator.getUserId(token);
        TodayDto todayDto = todayService.fetchByLocalDate(date, memberId);
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
