package com.example.everyhealth.config;

import com.example.everyhealth.domain.*;
import com.example.everyhealth.dto.TodayExerciseAsExerciseRequest;
import com.example.everyhealth.dto.ExerciseInfo;
import com.example.everyhealth.service.*;
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
    private final ChatRoomService chatRoomService;

    @PostConstruct
    public void init() {
        loadTestData();
    }

    public void loadTestData(){
        Member member = new Member("test", "test", MemberRole.USER, "google", "picture");
        memberService.save(member);

        for (int j = 1; j <= 20; j++) {
            List<ArrayList<Integer>> repWeights = new ArrayList<>();
            ArrayList<Integer> weight1 = new ArrayList<>(List.of(5, 5, 5));

            repWeights.add(weight1);
            Exercise exercise = new Exercise("name" + j, member, "memo" + j, repWeights, Classification.ABS);

            exerciseService.save(exercise);
        }
    }

}
