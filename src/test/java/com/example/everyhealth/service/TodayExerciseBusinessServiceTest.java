package com.example.everyhealth.service;

import com.example.everyhealth.domain.*;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "file:C:/coding/spring/project/everyhealth/src/main/resources/.env-local")
@Slf4j
class TodayExerciseBusinessServiceTest {

    @Autowired
    TodayExerciseBusinessService todayExerciseBusinessService;

    @Autowired
    TodayBusinessService todayBusinessService;

    @Autowired
    TodayExerciseDataService todayExerciseDataService;

    @Autowired
    TodayDataService todayDataService;

    @Autowired
    MemberService memberService;

    @Autowired
    ExerciseDataService exerciseDataService;

    @Autowired
    EntityManager em;


    private Long memberId;
    private Long member2Id;
    private Member member;
    private Member member2;

    @BeforeEach
    void init() {
        // 멤버 생성 및 저장
        member = new Member("길동", "test1", MemberRole.USER, "test1", "test1");
        memberId = memberService.save(member);

        member2 = new Member("홍길", "test2", MemberRole.USER, "test2", "test2");
        member2Id = memberService.save(member2);
    }

    @Test
    @DisplayName("Today 안에 TodayExercise가 없다면 삭제")
    void deleteTodayExerciseCascadeToday() {
        Long todayId = todayBusinessService.createToday(memberId, LocalDate.of(2025, 5, 30));
        Today today = todayDataService.findById(todayId);
        Exercise exercise = new Exercise("푸쉬업", member, "", Classification.CHEST);
        exerciseDataService.save(exercise);
        RepWeight repWeight1 = new RepWeight(20, 0, exercise);
        RepWeight repWeight2 = new RepWeight(20, 0, exercise);
        RepWeight repWeight3 = new RepWeight(20, 0, exercise);

        TodayExercise todayExercise = new TodayExercise(exercise, today, 1);
        todayExerciseDataService.save(todayExercise);

        todayExerciseBusinessService.deleteTodayExercise(todayExercise.getId());

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            todayExerciseDataService.findById(todayExercise.getId());
        });

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            todayDataService.findById(today.getId());
        });
    }

    @Test
    @DisplayName("TodayExercise 객체가 두 개 이상이라면 Today는 삭제X")
    void deleteTodayExercise() {
        Long todayId = todayBusinessService.createToday(memberId, LocalDate.of(2025, 5, 30));
        Today today = todayDataService.findById(todayId);
        Exercise exercise = new Exercise("푸쉬업", member, "", Classification.CHEST);
        exerciseDataService.save(exercise);
        RepWeight repWeight1 = new RepWeight(20, 0, exercise);
        RepWeight repWeight2 = new RepWeight(20, 0, exercise);
        RepWeight repWeight3 = new RepWeight(20, 0, exercise);

        TodayExercise todayExercise = new TodayExercise(exercise, today, 1);
        TodayExercise todayExercise2 = new TodayExercise(exercise, today, 2);
        todayExerciseDataService.save(todayExercise);
        todayExerciseDataService.save(todayExercise2);

        String result = todayExerciseBusinessService.deleteTodayExercise(todayExercise.getId());

        em.clear();
        Today findToday = todayDataService.findById(today.getId());

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            todayExerciseDataService.findById(todayExercise.getId());
        });

        org.assertj.core.api.Assertions.assertThat(findToday.getTodayExercises().size()).isEqualTo(1);
    }
}