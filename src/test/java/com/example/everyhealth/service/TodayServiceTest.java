package com.example.everyhealth.service;

import com.example.everyhealth.domain.Exercise;
import com.example.everyhealth.domain.Member;
import com.example.everyhealth.domain.Today;
import com.example.everyhealth.domain.TodayExercise;
import com.example.everyhealth.dto.TodayExerciseAsExerciseRequest;
import com.example.everyhealth.dto.ExerciseInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Slf4j
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

        ArrayList<ArrayList<Integer>> set = new ArrayList();
        set.add(new ArrayList<>(Arrays.asList(10,20)));
        set.add(new ArrayList<>(Arrays.asList(10,25)));
        set.add(new ArrayList<>(Arrays.asList(10,30)));

        Exercise exercise = new Exercise("푸쉬업", member, "무릎꿇고", set, "가슴");
        Long exerciseId1 = exerciseService.save(exercise);

        ArrayList<ArrayList<Integer>> set2 = new ArrayList<>();
        set2.add(new ArrayList<>(Arrays.asList(10,0)));
        set2.add(new ArrayList<>(Arrays.asList(10,0)));
        set2.add(new ArrayList<>(Arrays.asList(10,0)));

        Exercise exercise2 = new Exercise("디클라인 푸쉬업", member, "무릎꿇고", set2, "가슴");
        Long exerciseId2 = exerciseService.save(exercise2);

        ArrayList<ArrayList<Integer>> set3 = new ArrayList<>();
        set3.add(new ArrayList<>(Arrays.asList(30,0)));
        set3.add(new ArrayList<>(Arrays.asList(20,0)));
        set3.add(new ArrayList<>(Arrays.asList(10,0)));

        Exercise exercise3 = new Exercise("인클라인 푸쉬업", member, "화이팅", set3, "가슴");
        Long exerciseId3 = exerciseService.save(exercise3);

        ArrayList<ArrayList<Integer>> set4 = new ArrayList<>();
        set4.add(new ArrayList<>(Arrays.asList(10,60)));
        set4.add(new ArrayList<>(Arrays.asList(8,65)));
        set4.add(new ArrayList<>(Arrays.asList(6,70)));

        Exercise exercise4 = new Exercise("벤치프레스", member, "최대한 많이", set4, "가슴");
        Long exerciseId4 = exerciseService.save(exercise4);


        Today today = new Today(LocalDate.now());
        Long todayId = todayService.save(today);

        List<ExerciseInfo> exerciseInfoList = new ArrayList<>();
        exerciseInfoList.add(new ExerciseInfo(exerciseId4,3));
        exerciseInfoList.add(new ExerciseInfo(exerciseId3,3));
        exerciseInfoList.add(new ExerciseInfo(exerciseId2,3));

        TodayExerciseAsExerciseRequest dto = new TodayExerciseAsExerciseRequest(today.getId(), exerciseInfoList);
        todayService.addTodayExerciseAsExercise(dto);

        Today findToday = todayService.findById(todayId);


        for (TodayExercise todayExercise : findToday.getTodayExercises()) {
            log.info("todayExercise : {}", todayExercise.getExercise().getName());
        }
    }
}
