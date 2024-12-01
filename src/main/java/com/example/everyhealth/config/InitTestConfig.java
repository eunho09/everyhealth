package com.example.everyhealth.config;

import com.example.everyhealth.domain.*;
import com.example.everyhealth.dto.TodayExerciseAsExerciseRequest;
import com.example.everyhealth.dto.ExerciseInfo;
import com.example.everyhealth.service.ExerciseService;
import com.example.everyhealth.service.MemberService;
import com.example.everyhealth.service.RoutineService;
import com.example.everyhealth.service.TodayService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitTestConfig {

    private final MemberService memberService;
    private final ExerciseService exerciseService;
    private final RoutineService routineService;
    private final TodayService todayService;

    @PostConstruct
    public void init() {
        initData();
    }


    public void initData() {
        log.info("기본 테스트 데이터 실행");

        Member member = new Member("홍길동", "test", MemberRole.USER, "test");
        Long memberId = memberService.save(member);

        ArrayList<ArrayList<Integer>> set1 = new ArrayList<>();
        set1.add(new ArrayList<>(Arrays.asList(10,0)));
        set1.add(new ArrayList<>(Arrays.asList(10,0)));
        set1.add(new ArrayList<>(Arrays.asList(10,0)));

        Exercise exercise = new Exercise("푸쉬업", member, "무릎꿇고", set1, Classification.CHEST);
        exerciseService.save(exercise);

        ArrayList<ArrayList<Integer>> set2 = new ArrayList<>();
        set2.add(new ArrayList<>(Arrays.asList(10,0)));
        set2.add(new ArrayList<>(Arrays.asList(10,0)));
        set2.add(new ArrayList<>(Arrays.asList(10,0)));

        Exercise exercise2 = new Exercise("디클라인 푸쉬업", member, "무릎꿇고", set2, Classification.CHEST);
        exerciseService.save(exercise2);

        ArrayList<ArrayList<Integer>> set3 = new ArrayList<>();
        set3.add(new ArrayList<>(Arrays.asList(30,0)));
        set3.add(new ArrayList<>(Arrays.asList(20,0)));
        set3.add(new ArrayList<>(Arrays.asList(10,0)));

        Exercise exercise3 = new Exercise("인클라인 푸쉬업", member, "화이팅", set3, Classification.CHEST);
        exerciseService.save(exercise3);

        ArrayList<ArrayList<Integer>> set4 = new ArrayList<>();
        set4.add(new ArrayList<>(Arrays.asList(10,60)));
        set4.add(new ArrayList<>(Arrays.asList(8,65)));
        set4.add(new ArrayList<>(Arrays.asList(6,70)));

        Exercise exercise4 = new Exercise("벤치프레스", member, "최대한 많이", set4, Classification.CHEST);
        exerciseService.save(exercise4);

        Routine routine = new Routine("일요일 가슴 루틴", member);
        routineService.save(routine);

        List<ExerciseInfo> exerciseInfoList = new ArrayList<>();
        exerciseInfoList.add(new ExerciseInfo(exercise.getId(), 1));
        exerciseInfoList.add(new ExerciseInfo(exercise2.getId(), 2));
        exerciseInfoList.add(new ExerciseInfo(exercise4.getId(), 3));

        routineService.addExercise(routine.getId(), exerciseInfoList);

        Routine routine1 = new Routine("토요일 가슴 루틴", member);
        routineService.save(routine1);

//        Today today1 = new Today(LocalDate.now());
//        todayService.save(today1);

        Today today2 = new Today(LocalDate.of(2024, 11, 18));
        Today today3 = new Today(LocalDate.of(2024, 11, 17));
        Today today4 = new Today(LocalDate.of(2024, 10, 21));

        todayService.save(today2);
//        todayService.save(today3);
//        todayService.save(today4);

        List<ExerciseInfo> exerciseInfoList1 = new ArrayList<>();
        exerciseInfoList1.add(new ExerciseInfo(exercise4.getId(), 1));
        exerciseInfoList1.add(new ExerciseInfo(exercise2.getId(), 2));

        TodayExerciseAsExerciseRequest dto = new TodayExerciseAsExerciseRequest(today2.getId(), exerciseInfoList1);

        todayService.addTodayExerciseAsExercise(dto);

    }

}
