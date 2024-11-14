package com.example.everyhealth.service;

import com.example.everyhealth.domain.Exercise;
import com.example.everyhealth.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;

@SpringBootTest
@Slf4j
public class ExercisesServiceTest {

    @Autowired
    ExerciseService exerciseService;

    @Autowired
    MemberService memberService;

    @Test
    @Transactional
    void save() {
        Member member = new Member("홍길동", "test", "test");
        Long memberId = memberService.save(member);

        ArrayList<ArrayList<Integer>> set = new ArrayList();
        set.add(new ArrayList<>(Arrays.asList(10,20)));
        set.add(new ArrayList<>(Arrays.asList(10,25)));
        set.add(new ArrayList<>(Arrays.asList(10,30)));

        Exercise exercise = new Exercise("푸쉬업", member, "무릎꿇고", set, "가슴");
        Long exerciseId = exerciseService.save(exercise);

        Exercise findExercise = exerciseService.findById(exerciseId);

        Assertions.assertThat(findExercise.getRepWeight().get(0)).containsExactly(10,20);
        Assertions.assertThat(findExercise.getRepWeight().get(1)).containsExactly(10,25);
        Assertions.assertThat(findExercise.getRepWeight().get(2)).containsExactly(10,30);

        log.info("set={}",findExercise.getRepWeight());

        exercise.setName("인클라인 푸쉬업");

        Assertions.assertThat(findExercise.getName()).isEqualTo("인클라인 푸쉬업");
    }
}

