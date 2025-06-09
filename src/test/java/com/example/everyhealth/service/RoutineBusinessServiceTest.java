package com.example.everyhealth.service;

import com.example.everyhealth.domain.*;
import com.example.everyhealth.dto.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@TestPropertySource(locations = "file:C:/coding/spring/project/everyhealth/src/main/resources/.env-local")
class RoutineBusinessServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    RoutineBusinessService routineBusinessService;
    @Autowired
    RoutineDataService routineDataService;
    @Autowired
    ExerciseBusinessService exerciseBusinessService;
    @Autowired
    RoutineExerciseService routineExerciseService;
    @Autowired
    EntityManager em;


    private Long memberId;
    private Long memberId2;

    private Long createExercise(String name, String memo, List<RepWeightDto> repWeightDtos, Classification classification, Long memberId) {
        ExerciseCreateDto exerciseCreateDto = new ExerciseCreateDto(name, memo, repWeightDtos, classification);
        return exerciseBusinessService.createExercise(memberId, exerciseCreateDto);
    }

    private Long createAndAddExercise(Long memberId, String routineName) {
        Long id = routineBusinessService.createRoutine(memberId, routineName);

        RepWeightDto repWeightDto = new RepWeightDto(50, 5);
        RepWeightDto repWeightDto1 = new RepWeightDto(50, 10);
        RepWeightDto repWeightDto2 = new RepWeightDto(50, 20);

        List<RepWeightDto> repWeightDtos = List.of(new RepWeightDto[]{repWeightDto, repWeightDto1, repWeightDto2});

        Long exerciseId = createExercise("스쿼트", "어깨넓이로", repWeightDtos, Classification.LOWERBODY, memberId);

        RepWeightDto repWeightDto3 = new RepWeightDto(50, 5);
        RepWeightDto repWeightDto4 = new RepWeightDto(50, 10);
        RepWeightDto repWeightDto5 = new RepWeightDto(50, 20);

        List<RepWeightDto> repWeightDtosPushUp = List.of(new RepWeightDto[]{repWeightDto3, repWeightDto4, repWeightDto5});
        Long exerciseId2 = createExercise("푸쉬업", "", repWeightDtosPushUp, Classification.CHEST, memberId);
        List<ExerciseInfo> exerciseInfos = List.of(new ExerciseInfo(exerciseId, 1), new ExerciseInfo(exerciseId2, 2));

        routineBusinessService.addExercise(id, exerciseInfos);

        return id;
    }

    @BeforeEach
    void init() {
        // 멤버 생성 및 저장
        Member member = new Member("길동", "test1", MemberRole.USER, "test1", "test1");
        memberId = memberService.save(member);

        Member member2 = new Member("홍길", "test2", MemberRole.USER, "test2", "test2");
        memberId2 = memberService.save(member2);
    }

    @Test
    @DisplayName("루틴 생성")
    void create() {
        Long id = routineBusinessService.createRoutine(memberId, "토요일 운동 루틴");
        em.clear();

        Routine routine = routineDataService.findById(id);
        Assertions.assertThat(routine.getName()).isEqualTo("토요일 운동 루틴");
    }

    @Test
    @DisplayName("루틴의 운동 추가")
    void addExercise() {
        Long id = createAndAddExercise(memberId, "토요일 운동 루틴");
        em.clear();

        List<RoutineExerciseResponseDto> routineExercise = routineBusinessService.getRoutine(id);

        Assertions.assertThat(routineExercise)
                .hasSize(2)
                .extracting(re -> re.getExerciseName())
                .contains("스쿼트", "푸쉬업");
    }

    @Test
    @DisplayName("루틴 삭제")
    void delete() {
        Long id = createAndAddExercise(memberId, "토요일 운동 루틴");
        routineBusinessService.deleteRoutine(id);

        em.clear();

        assertThrows(EntityNotFoundException.class, () -> routineDataService.findById(id));
        Assertions.assertThat(routineExerciseService.findByRoutineId(id)).isEmpty();
    }

    @Test
    @DisplayName("루틴의 운동 삭제")
    void deleteRoutineExercise() {
        Long id = createAndAddExercise(memberId, "토요일 운동 루틴");

        List<RoutineExercise> routineExercises = routineExerciseService.findByRoutineId(id);

        Long routineExerciseId = 0L;

        for (RoutineExercise routineExercise : routineExercises){
            if (routineExercise.getExercise().getName().equals("푸쉬업")) {
                routineExerciseId = routineExercise.getId();
            }
        }

        routineBusinessService.deleteRoutineExercise(routineExerciseId);

        em.clear();

        List<RoutineExerciseResponseDto> routineExerciseList = routineBusinessService.getRoutine(id);

        Assertions.assertThat(routineExerciseList)
                .hasSize(1)
                .extracting(re -> re.getExerciseName())
                .contains("스쿼트");
    }

    @Test
    @DisplayName("나의 루틴 조회")
    void memberRoutine() {
        createAndAddExercise(memberId, "토요일 운동 루틴");
        createAndAddExercise(memberId, "일요일 운동 루틴");
        createAndAddExercise(memberId2, "월요일 운동 루틴");

        em.clear();
        List<RoutineResponseDto> routines = routineBusinessService.fetchRoutinesByMemberId(memberId);

        Assertions.assertThat(routines)
                .hasSize(2)
                .extracting(r -> r.getRoutineName())
                .contains("토요일 운동 루틴", "일요일 운동 루틴");
    }

}