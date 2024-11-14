/*
package com.example.everyhealth.service;

import com.example.everyhealth.domain.Exercise;
import com.example.everyhealth.domain.Member;
import com.example.everyhealth.domain.Routine;
import com.example.everyhealth.domain.RoutineExercise;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RoutineServiceTest {

    @Autowired
    RoutineService routineService;

    @Autowired
    MemberService memberService;

    @Autowired
    ExerciseService exerciseService;

    @Autowired
    RoutineExerciseService routineExerciseService;

    //시간 : 127
    @Test
    void save() {
        long startTime = System.currentTimeMillis();
        Member member = new Member("홍길동", "test", "test");
        Long memberId = memberService.save(member);

        Member findMember = memberService.findById(memberId);


        List<ArrayList<Integer>> set = new ArrayList<>();
        set.add(new ArrayList<>(Arrays.asList(10,20)));
        set.add(new ArrayList<>(Arrays.asList(10,20)));
        set.add(new ArrayList<>(Arrays.asList(10,20)));

        Exercise exercise1 = new Exercise("푸쉬업", findMember, "무릎꿇고", set, "가슴");
        Exercise exercise2 = new Exercise("파이크 푸쉬업", findMember, "무릎꿇고", set, "가슴");
        Exercise exercise3 = new Exercise("인클라인 푸쉬업", findMember, "무릎꿇고", set, "가슴");
        Long findExId1 = exerciseService.save(exercise1);
        Long findExId2 = exerciseService.save(exercise2);
        Long findExId3 = exerciseService.save(exercise3);

        Exercise findEx1 = exerciseService.findById(findExId1);
        Exercise findEx2 = exerciseService.findById(findExId2);
        Exercise findEx3 = exerciseService.findById(findExId3);

        Routine routine = new Routine("일요일 가슴운동 루틴");
        Long routineId = routineService.save(routine);
        Routine findRoutine = routineService.findById(routineId);

        RoutineExercise routineExercise1 = new RoutineExercise(findEx1, findRoutine);
        RoutineExercise routineExercise2 = new RoutineExercise(findEx2, findRoutine);

        routineExerciseService.save(routineExercise1);
        routineExerciseService.save(routineExercise2);
        long endTime = System.currentTimeMillis();
        System.out.println("시간" + (endTime - startTime));

        Assertions.assertThat(findRoutine.getRoutineExerciseList().get(0).getExercise().getName()).isEqualTo("푸쉬업");
        Assertions.assertThat(findRoutine.getRoutineExerciseList().get(1).getExercise().getName()).isEqualTo("파이크 푸쉬업");
    }


    //시간 67
    @Test
    void cascade() {
        long startTime = System.currentTimeMillis();
        Member member = new Member("홍길동", "test", "test");
        Long memberId = memberService.save(member);

        Member findMember = memberService.findById(memberId);

        List<ArrayList<Integer>> set = new ArrayList<>();
        set.add(new ArrayList<>(Arrays.asList(10,20)));
        set.add(new ArrayList<>(Arrays.asList(10,20)));
        set.add(new ArrayList<>(Arrays.asList(10,20)));

        Exercise exercise1 = new Exercise("푸쉬업", findMember, "무릎꿇고", set, "가슴");
        Exercise exercise2 = new Exercise("파이크 푸쉬업", findMember, "무릎꿇고", set, "가슴");
        Exercise exercise3 = new Exercise("인클라인 푸쉬업", findMember, "무릎꿇고", set, "가슴");
        Long findExId1 = exerciseService.save(exercise1);
        Long findExId2 = exerciseService.save(exercise2);
        Long findExId3 = exerciseService.save(exercise3);

        Routine routine = new Routine("일요일 가슴운동 루틴");
        Long routineId = routineService.save(routine);

        List<Long> exArr = new ArrayList<>(Arrays.asList(findExId1, findExId2));
        Routine saveRoutine = routineService.addExercise(routineId, exArr);

        long endTime = System.currentTimeMillis();
        System.out.println("시간" + (endTime - startTime));

        Assertions.assertThat(saveRoutine.getRoutineExerciseList().get(0).getExercise().getName()).isEqualTo("푸쉬업");
        Assertions.assertThat(saveRoutine.getRoutineExerciseList().get(1).getExercise().getName()).isEqualTo("파이크 푸쉬업");
    }
}
*/
