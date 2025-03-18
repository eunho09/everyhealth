package com.example.everyhealth.controller;

import com.example.everyhealth.aop.ExtractMemberId;
import com.example.everyhealth.domain.*;
import com.example.everyhealth.dto.*;
import com.example.everyhealth.security.JwtTokenGenerator;
import com.example.everyhealth.service.FriendService;
import com.example.everyhealth.service.MemberService;
import com.example.everyhealth.service.TodayExerciseService;
import com.example.everyhealth.service.TodayService;
import jakarta.transaction.Transactional;
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

    private final TodayService todayService;
    private final TodayExerciseService todayExerciseService;
    private final MemberService memberService;
    private final FriendService friendService;


    @PostMapping("/today")
    public ResponseEntity<Void> save(@ExtractMemberId Long memberId) {
        Member member = memberService.findById(memberId);
        Today today = new Today(LocalDate.now(), member);
        todayService.save(today);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/today/addTodayExercise/{date}")
    public ResponseEntity<String> addTodayExercise(@ExtractMemberId Long memberId ,@RequestBody List<TodayExerciseRequest> dto, @PathVariable LocalDate date) {
        todayService.addTodayExercise(dto, date, memberId);

        return ResponseEntity.ok("add todayExercise");
    }

    @GetMapping("/member/todays")
    public ResponseEntity<List<TodayDto>> memberTodays(@ExtractMemberId Long memberId) {
        List<Today> todays = todayService.fetchMemberId(memberId);

        List<TodayDto> todayList = todays.stream()
                .map(t -> {
                    List<TodayExercise> todayExercises = t.getTodayExercises();
                    List<TodayExerciseDto> list = todayExercises.stream()
                            .map(te -> new TodayExerciseDto(
                                    te.getId(),
                                    te.getExercise().getName(),
                                    te.getRepWeightList().stream()
                                            .map(rw -> new RepWeightDto(rw.getId(), rw.getReps(), rw.getWeight()))
                                            .toList(),
                                    te.getSequence()))
                            .toList();

                    return new TodayDto(t.getId(),
                            list,
                            t.getLocalDate(),
                            t.getCheckBox());
                }).toList();

        return ResponseEntity.ok(todayList);
    }



    @GetMapping("/today/{todayId}")
    public ResponseEntity<TodayDto> findById(@PathVariable Long todayId) {

        Today today = todayService.findById(todayId);
        List<TodayExercise> todayExerciseList = todayExerciseService.findByTodayId(todayId);

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


    @GetMapping("/today/month/{month}")
    public ResponseEntity<List<TodayDateDto>> findByMonth(@ExtractMemberId Long memberId, @PathVariable int month) {
        List<TodayDateDto> todayList = todayService.findByMonth(month, memberId);
        return ResponseEntity.ok(todayList);
    }

    @GetMapping("/today/friendAndMonth/{friendId}/{month}")
    public ResponseEntity<List<TodayDateDto>> findByFriendAndMonth(
            @PathVariable Long friendId,
            @PathVariable int month) {
        Member member = memberService.findByFriendInfo(friendId);
        List<TodayDateDto> todayList = todayService.findByMonth(month, member.getId());
        return ResponseEntity.ok(todayList);
    }


    @GetMapping("/today/date/{date}")
    public ResponseEntity<TodayDto> fetchByLocalDate(@ExtractMemberId Long memberId, @PathVariable LocalDate date) {
        TodayDto todayDto = todayService.fetchByLocalDate(date, memberId);
        return ResponseEntity.ok(todayDto);
    }

    @GetMapping("/today/friendAndDate/{friendId}/{date}")
    public ResponseEntity<TodayDto> fetchByLocalDateAndFriendId(
            @PathVariable LocalDate date,
            @PathVariable Long friendId) {
        Member member = memberService.findByFriendInfo(friendId);
        TodayDto todayDto = todayService.fetchByLocalDate(date, member.getId());
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

    @PostMapping("/today/checkbox/{todayId}")
    public ResponseEntity<String> checkbox(@PathVariable Long todayId, @RequestParam boolean checked) {
        todayService.updateCheckbox(checked, todayId);

        return ResponseEntity.ok("update checkbox");
    }
}
