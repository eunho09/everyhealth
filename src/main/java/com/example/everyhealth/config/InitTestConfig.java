package com.example.everyhealth.config;

import com.example.everyhealth.domain.Exercise;
import com.example.everyhealth.domain.Member;
import com.example.everyhealth.service.ExerciseService;
import com.example.everyhealth.service.MemberService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitTestConfig {

    private final MemberService memberService;
    private final ExerciseService exerciseService;

    @PostConstruct
    public void init() {
        initData();
    }


    public void initData() {
        log.info("기본 테스트 데이터 실행");

        Member member = new Member("홍길동", "test", "test");
        Long memberId = memberService.save(member);

        ArrayList<ArrayList<Integer>> set1 = new ArrayList<>();
        set1.add(new ArrayList<>(Arrays.asList(10,0)));
        set1.add(new ArrayList<>(Arrays.asList(10,0)));
        set1.add(new ArrayList<>(Arrays.asList(10,0)));

        Exercise exercise = new Exercise("푸쉬업", member, "무릎꿇고", set1, "가슴");
        exerciseService.save(exercise);

        ArrayList<ArrayList<Integer>> set2 = new ArrayList<>();
        set2.add(new ArrayList<>(Arrays.asList(10,0)));
        set2.add(new ArrayList<>(Arrays.asList(10,0)));
        set2.add(new ArrayList<>(Arrays.asList(10,0)));

        Exercise exercise2 = new Exercise("디클라인 푸쉬업", member, "무릎꿇고", set2, "가슴");
        exerciseService.save(exercise2);

        ArrayList<ArrayList<Integer>> set3 = new ArrayList<>();
        set3.add(new ArrayList<>(Arrays.asList(30,0)));
        set3.add(new ArrayList<>(Arrays.asList(20,0)));
        set3.add(new ArrayList<>(Arrays.asList(10,0)));

        Exercise exercise3 = new Exercise("인클라인 푸쉬업", member, "화이팅", set3, "가슴");
        exerciseService.save(exercise3);

        ArrayList<ArrayList<Integer>> set4 = new ArrayList<>();
        set4.add(new ArrayList<>(Arrays.asList(10,60)));
        set4.add(new ArrayList<>(Arrays.asList(8,65)));
        set4.add(new ArrayList<>(Arrays.asList(6,70)));

        Exercise exercise4 = new Exercise("벤치프레스", member, "최대한 많이", set4, "가슴");
        exerciseService.save(exercise4);
    }

}
