package com.example.everyhealth.controller;

import com.example.everyhealth.aop.ExtractMemberId;
import com.example.everyhealth.domain.*;
import com.example.everyhealth.dto.*;
import com.example.everyhealth.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
@Tag(name = "오늘 관리")
public class RestTodayController {

    private final TodayDataService todayDataService;
    private final TodayBusinessService todayBusinessService;
    private final TodayExerciseDataService todayExerciseDataService;
    private final MemberService memberService;
    private final TodayExerciseBusinessService todayExerciseBusinessService;


    @PostMapping("/today")
    @Operation(summary = "오늘 저장")
    public ResponseEntity<String> save(@ExtractMemberId Long memberId, @RequestParam LocalDate date) {
        todayBusinessService.createToday(memberId, date);
        return ResponseEntity.status(HttpStatus.CREATED).body(date + "의 Today를 생성했습니다.");
    }

    @PostMapping("/todayExercise/{date}")
    @Operation(summary = "오늘의 운동 추가")
    public ResponseEntity<String> addTodayExercise(@ExtractMemberId Long memberId ,@RequestBody List<TodayExerciseRequest> dto, @PathVariable LocalDate date) {
        todayBusinessService.addTodayExercise(dto, date, memberId);

        return ResponseEntity.ok("add todayExercise");
    }

    @GetMapping("/member/todays")
    @Operation(summary = "나의 오늘 조회")
    public ResponseEntity<List<TodayDto>> memberTodays(@ExtractMemberId Long memberId) {
        List<TodayDto> todayDtos = todayBusinessService.memberTodays(memberId);

        return ResponseEntity.ok(todayDtos);
    }



    @GetMapping("/today/{todayId}")
    @Operation(summary = "오늘 조회")
    public ResponseEntity<TodayDto> findById(@PathVariable Long todayId) {
        TodayDto todayDto = todayBusinessService.fetchById(todayId);

        return ResponseEntity.ok(todayDto);
    }


    @GetMapping("/today/yearAndMonth/{year}/{month}")
    @Operation(summary = "년/월로 나의 오늘 조회")
    public ResponseEntity<List<TodayDateDto>> findByYearAndMonth(@ExtractMemberId Long memberId,
                                                                 @PathVariable int year,
                                                                 @PathVariable int month) {
        List<TodayDateDto> todayList = todayDataService.findByYearAndMonth(year, month, memberId);
        return ResponseEntity.ok(todayList);
    }

    @GetMapping("/today/friend/{friendId}/yearAndMonth/{year}/{month}")
    @Operation(summary = "년/월, 친구상태로 친구의 오늘 조회")
    public ResponseEntity<List<TodayDateDto>> findByFriendAndMonth(
            @ExtractMemberId Long memberId,
            @PathVariable Long friendId,
            @PathVariable int year,
            @PathVariable int month) {
        memberService.existsByIdAndFriendId(memberId, friendId);

        List<TodayDateDto> todayList = todayDataService.findByYearAndMonth(year, month, friendId);

        return ResponseEntity.ok(todayList);
    }


    @GetMapping("/today/date/{date}")
    @Operation(summary = "날짜로 오늘 조회")
    public ResponseEntity<TodayDto> fetchByLocalDate(@ExtractMemberId Long memberId, @PathVariable LocalDate date) {
        TodayDto todayDto = todayDataService.fetchByLocalDate(date, memberId);
        return ResponseEntity.ok(todayDto);
    }

    @GetMapping("/today/friend/{friendId}/date/{date}")
    @Operation(summary = "날짜와 친구의 상태로 친구의 오늘")
    public ResponseEntity<TodayDto> fetchByLocalDateAndFriendId(
            @ExtractMemberId Long memberId,
            @PathVariable LocalDate date,
            @PathVariable Long friendId) {

        memberService.existsByIdAndFriendId(memberId, friendId);
        TodayDto todayDto = todayDataService.fetchByLocalDate(date, friendId);
        return ResponseEntity.ok(todayDto);
    }


    @PatchMapping("/todayExercise/{todayId}")
    @Operation(summary = "오늘의 운동 업데이트")
    public ResponseEntity<String> updateTodayExercise(@RequestBody List<UpdateTodayExerciseDto> dto, @PathVariable Long todayId) {
        todayBusinessService.updateTodayExercise(dto, todayId);
        return ResponseEntity.ok("update TodayExercise");
    }

    @DeleteMapping("/todayExercise/{id}")
    @Operation(summary = "오늘의 운동 삭제")
    public ResponseEntity<String> deleteTodayExercise(@PathVariable Long id) {
        String resultMessage = todayExerciseBusinessService.deleteTodayExercise(id);
        return ResponseEntity.ok(resultMessage);
    }

    @PatchMapping("/todayExercise/updateSequence/{todayId}")
    @Operation(summary = "오늘의 운동 순서 업데이트")
    public ResponseEntity<String> updateSequence(@RequestBody List<UpdateSeqTodayExercise> todayExerciseList, @PathVariable Long todayId) {
        todayBusinessService.updateSequence(todayExerciseList, todayId);

        return ResponseEntity.ok("update sequence todayExercise");
    }

    @PostMapping("/today/checkbox/{todayId}")
    @Operation(summary = "오늘의 완료 상태 업데이트")
    public ResponseEntity<String> checkbox(@PathVariable Long todayId, @RequestParam boolean checked) {
        todayBusinessService.updateCheckbox(checked, todayId);

        return ResponseEntity.ok("update checkbox");
    }
}
