package com.example.everyhealth.service;

import com.example.everyhealth.domain.Exercise;
import com.example.everyhealth.domain.Member;
import com.example.everyhealth.domain.Today;
import com.example.everyhealth.domain.TodayExercise;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TodayServiceTest {

    @Autowired
    TodayService todayService;

    @Autowired
    MemberService memberService;

    @Autowired
    ExerciseService exerciseService;

    @Autowired
    TodayExerciseService todayExerciseService;

    @Test
    void save() {
        Member member = new Member("홍길동", "test", "test");
        Long memberId = memberService.save(member);

        Member findMember = memberService.findById(memberId);

        ArrayList<Integer> repetitions = new ArrayList(Arrays.asList(10,20,30));
        ArrayList<Integer> weights = new ArrayList(Arrays.asList(10,15,20));

        Exercise exercise1 = new Exercise("푸쉬업", findMember, "무릎꿇고", repetitions, weights, "가슴");
        Exercise exercise2 = new Exercise("파이크 푸쉬업", findMember, "무릎꿇고", repetitions, weights, "가슴");
        Exercise exercise3 = new Exercise("인클라인 푸쉬업", findMember, "무릎꿇고", repetitions, weights, "가슴");
        Long findExId1 = exerciseService.save(exercise1);
        Long findExId2 = exerciseService.save(exercise2);
        Long findExId3 = exerciseService.save(exercise3);

        Exercise findEx1 = exerciseService.findById(findExId1);
        Exercise findEx2 = exerciseService.findById(findExId2);

        Today today = new Today();
        Long findTodayId = todayService.save(today);
        Today findToday = todayService.findById(findTodayId);

        TodayExercise todayExercise = new TodayExercise(findEx1, findToday);
        Long todayExerciseId = todayExerciseService.save(todayExercise);
        TodayExercise findTodayExercise = todayExerciseService.findById(todayExerciseId);

        TodayExercise todayExercise2 = new TodayExercise(findEx2, findToday);
        Long todayExerciseId2 = todayExerciseService.save(todayExercise2);
        TodayExercise findTodayExercise2 = todayExerciseService.findById(todayExerciseId2);

        Assertions.assertThat(findTodayExercise.getExercise().getName()).isEqualTo("푸쉬업");
        Assertions.assertThat(findTodayExercise2.getRepetitions()).containsExactly(10, 20, 30);

        ArrayList<Integer> changeRepetitions = new ArrayList<>(Arrays.asList(50, 60, 60));
        findTodayExercise2.setRepetitions(changeRepetitions);
        Assertions.assertThat(findTodayExercise2.getRepetitions()).containsExactly(50, 60, 60);
        Assertions.assertThat(findTodayExercise2.getExercise().getRepetitions()).containsExactly(10, 20, 30);
    }
}