package com.example.everyhealth.config.test;

import com.example.everyhealth.domain.*;
import com.example.everyhealth.service.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Profile("test")
@RequiredArgsConstructor
@Slf4j
public class InitTestConfig {

    private final MemberService memberService;
    private final ExerciseDataService exerciseDataService;
    private final RoutineDataService routineDataService;
    private final RoutineExerciseService routineExerciseService;
    private final TodayDataService todayDataService;
    private final TodayExerciseDataService todayExerciseDataService;
    private final ChatMessageService chatMessageService;
    private final ClubDataService clubDataService;
    private final ChatRoomService chatRoomService;
    private final ClubMemberService clubMemberService;

    @PostConstruct
    public void init() {
//        loadExerciseTestData();
//        loadRoutineTestData();
//        loadTodayTestData();
//        loadMessageTestData();
//        loadMemberTestData();
    }

    private void loadMemberTestData(){
        List<Member> memberList = memberService.findAll();
        if (memberList.isEmpty()){
            Member member = new Member("test", "test", MemberRole.USER, "google", "picture");
            memberService.save(member);
        }
    }

    public void loadExerciseTestData(){
        Member member = new Member("test", "test", MemberRole.USER, "google", "picture");
        memberService.save(member);

        for (int i = 1; i <= 20; i++) {

            Exercise exercise = new Exercise("name" + i, member, "memo" + i, Classification.ABS);
            new RepWeight(i, i, exercise);
            new RepWeight(i * 2 , i * 2, exercise);
            new RepWeight(i * 3, i * 3, exercise);

            exerciseDataService.save(exercise);
        }
    }

/*    public void loadRoutineTestData(){
        Member member = new Member("test", "test", MemberRole.USER, "google", "picture");
        memberService.save(member);

        for (int i = 1; i <= 20; i++){
            Routine routine = new Routine(i + "name", member);
            routineService.save(routine);

            for (int j = 1; j <= 20; j++) {
                List<ArrayList<Integer>> repWeights = new ArrayList<>();
                ArrayList<Integer> repweight1 = new ArrayList<>(List.of(10, 5));
                ArrayList<Integer> repweight2 = new ArrayList<>(List.of(10, 5));
                ArrayList<Integer> repweight3 = new ArrayList<>(List.of(10, 5));

                repWeights.add(repweight1);
                repWeights.add(repweight2);
                repWeights.add(repweight3);

                Exercise exercise = new Exercise("name" + j, member, "memo" + j, repWeights, Classification.ABS);

                exerciseService.save(exercise);

                RoutineExercise routineExercise = new RoutineExercise(exercise, routine, j, repWeights);
                routineExerciseService.save(routineExercise);
            }
        }
    }

    public void loadTodayTestData() {
        Member member = new Member("test", "test", MemberRole.USER, "google", "picture");
        memberService.save(member);

        for (int i = 1; i <= 20; i++) {
            Today today = new Today(LocalDate.of(2025, 5, i), member);
            todayService.save(today);

            for (int j = 1; j <= 20; j++) {
                List<ArrayList<Integer>> repWeights = new ArrayList<>();
                ArrayList<Integer> repweight1 = new ArrayList<>(List.of(10, 5));
                ArrayList<Integer> repweight2 = new ArrayList<>(List.of(10, 5));
                ArrayList<Integer> repweight3 = new ArrayList<>(List.of(10, 5));

                repWeights.add(repweight1);
                repWeights.add(repweight2);
                repWeights.add(repweight3);

                Exercise exercise = new Exercise("name" + j, member, "memo" + j, repWeights, Classification.ABS);

                exerciseService.save(exercise);

                TodayExercise todayExercise = new TodayExercise(exercise, today, repWeights, j);
                todayExerciseService.save(todayExercise);
            }
        }
    }

    public void loadMessageTestData() {
        Member member = new Member("test", "test", MemberRole.USER, "google", "picture");
        memberService.save(member);

        ArrayList<String> highlights = new ArrayList<>(List.of("1", "2", "3"));

        Club club = new Club("title", "content", "location", "schedule", highlights);
        clubService.save(club);
        ChatRoom chatRoom = new ChatRoom(club.getTitle(), club);
        chatRoomService.save(chatRoom);

        ClubMember clubMember = new ClubMember(club, member, true, LocalDateTime.now());
        clubMemberService.save(clubMember);
        for (int i = 1; i <= 20; i++) {
            chatMessageService.saveMessage("message" + i, chatRoom.getId(), clubMember.getMember().getId());
        }
    }*/

}
