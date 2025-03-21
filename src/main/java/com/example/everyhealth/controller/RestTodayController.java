package com.example.everyhealth.controller;

import com.example.everyhealth.aop.ExtractMemberId;
import com.example.everyhealth.domain.*;
import com.example.everyhealth.dto.*;
import com.example.everyhealth.service.*;
import lombok.RequiredArgsConstructor;
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
public class RestTodayController {

    private final TodayService todayService;
    private final TodayExerciseService todayExerciseService;
    private final MemberService memberService;
    private final RepWeightService repWeightService;


    @PostMapping("/today")
    public ResponseEntity<String> save(@ExtractMemberId Long memberId, @RequestParam LocalDate date) {
        Member member = memberService.findById(memberId);
        Today today = new Today(date, member);
        todayService.save(today);

        return ResponseEntity.status(HttpStatus.CREATED).body(date + "의 Today를 생성했습니다.");
    }

    @PostMapping("/todayExercise/{date}")
    public ResponseEntity<String> addTodayExercise(@ExtractMemberId Long memberId ,@RequestBody List<TodayExerciseRequest> dto, @PathVariable LocalDate date) {
        todayService.addTodayExercise(dto, date, memberId);

        return ResponseEntity.ok("add todayExercise");
    }

    @GetMapping("/member/todays")
    public ResponseEntity<List<TodayDto>> memberTodays(@ExtractMemberId Long memberId) {
        List<Today> todays = todayService.fetchMemberId(memberId);
        List<TodayExercise> todayExercises = todayExerciseService.fetchByTodayIdIn(todays.stream().map(t -> t.getId()).toList());

        Map<Long, TodayExercise> todayExerciseMap = todayExercises.stream()
                .collect(Collectors.toMap(te -> te.getId(), dto -> dto));

        List<TodayDto> responseList = todays.stream()
                .map(t -> {
                    return new TodayDto(
                            t.getId(),
                            t.getTodayExercises().stream().map(
                                    te -> {
                                        TodayExercise fetchTe = todayExerciseMap.get(te.getId());
                                        return new TodayExerciseDto(
                                                fetchTe.getId(),
                                                fetchTe.getExercise().getName(),
                                                fetchTe.getRepWeightList().stream()
                                                        .map(rw -> new RepWeightDto(
                                                                rw.getId(),
                                                                rw.getReps(),
                                                                rw.getWeight()
                                                        )).toList(),
                                                te.getSequence()
                                        );
                                    }
                            ).toList(),
                            t.getLocalDate(),
                            t.getCheckBox()
                    );
                }).toList();

        return ResponseEntity.ok(responseList);
    }



    @GetMapping("/today/{todayId}")
    public ResponseEntity<TodayDto> findById(@PathVariable Long todayId) {

        Today today = todayService.findById(todayId);
        List<TodayExercise> todayExerciseList = todayExerciseService.fetchByTodayId(todayId);

        List<TodayExerciseDto> todayExerciseDtoList = todayExerciseList.stream()
                .map(todayExercise -> new TodayExerciseDto(
                        todayExercise.getId(),
                        todayExercise.getExercise().getName(),
                        todayExercise.getRepWeightList().stream()
                                .map(rw -> new RepWeightDto(rw.getId(), rw.getReps(), rw.getWeight()))
                                .toList(),
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


    @GetMapping("/today/yearAndMonth/{year}/{month}")
    public ResponseEntity<List<TodayDateDto>> findByYearAndMonth(@ExtractMemberId Long memberId,
                                                                 @PathVariable int year,
                                                                 @PathVariable int month) {
        List<TodayDateDto> todayList = todayService.findByYearAndMonth(year, month, memberId);
        return ResponseEntity.ok(todayList);
    }

    @GetMapping("/today/friend/{friendId}/yearAndMonth/{year}/{month}")
    public ResponseEntity<List<TodayDateDto>> findByFriendAndMonth(
            @ExtractMemberId Long memberId,
            @PathVariable Long friendId,
            @PathVariable int year,
            @PathVariable int month) {
        boolean exists = memberService.existsByIdAndFriendId(memberId, friendId);
        if (!exists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Long findMemberId = memberService.findIdByFriendId(friendId);
        List<TodayDateDto> todayList = todayService.findByYearAndMonth(year, month, findMemberId);

        return ResponseEntity.ok(todayList);
    }


    @GetMapping("/today/date/{date}")
    public ResponseEntity<TodayDto> fetchByLocalDate(@ExtractMemberId Long memberId, @PathVariable LocalDate date) {
        TodayDto todayDto = todayService.fetchByLocalDate(date, memberId);
        return ResponseEntity.ok(todayDto);
    }

    @GetMapping("/today/friend/{friendId}/date/{date}")
    public ResponseEntity<TodayDto> fetchByLocalDateAndFriendId(
            @ExtractMemberId Long memberId,
            @PathVariable LocalDate date,
            @PathVariable Long friendId) {

        boolean exists = memberService.existsByIdAndFriendId(memberId, friendId);
        if (!exists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Long findMemberId = memberService.findIdByFriendId(friendId);
        TodayDto todayDto = todayService.fetchByLocalDate(date, findMemberId);
        return ResponseEntity.ok(todayDto);
    }


    @PatchMapping("/todayExercise/{todayId}")
    public ResponseEntity<String> updateTodayExercise(@RequestBody List<UpdateTodayExerciseDto> dto, @PathVariable Long todayId) {
        todayService.updateTodayExercise(dto, todayId);
        return ResponseEntity.ok("update TodayExercise");
    }

    @DeleteMapping("/todayExercise/{id}")
    public ResponseEntity<String> deleteTodayExercise(@PathVariable Long id) {
        repWeightService.deleteByTodayExerciseId(id);
        todayExerciseService.deleteById(id);
        return ResponseEntity.ok("delete todayExercise");
    }

    @PatchMapping("/todayExercise/updateSequence/{todayId}")
    public ResponseEntity<String> updateSequence(@RequestBody List<UpdateSeqTodayExercise> todayExerciseList, @PathVariable Long todayId) {
        todayService.updateSequence(todayExerciseList, todayId);

        return ResponseEntity.ok("update sequence todayExercise");
    }

    @PostMapping("/today/checkbox/{todayId}")
    public ResponseEntity<String> checkbox(@PathVariable Long todayId, @RequestParam boolean checked) {
        todayService.updateCheckbox(checked, todayId);

        return ResponseEntity.ok("update checkbox");
    }
}
