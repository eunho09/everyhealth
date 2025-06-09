package com.example.everyhealth.service;

import com.example.everyhealth.domain.*;
import com.example.everyhealth.dto.ExerciseCreateDto;
import com.example.everyhealth.dto.ExerciseResponseDto;
import com.example.everyhealth.dto.RepWeightDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "file:C:/coding/spring/project/everyhealth/src/main/resources/.env-local")
@Slf4j
class ExerciseBusinessServiceTest {

    @Autowired
    ExerciseBusinessService exerciseBusinessService;
    @Autowired
    ExerciseDataService exerciseDataService;
    @Autowired
    MemberService memberService;
    @Autowired
    EntityManager em;

    public Long memberId;
    public Long memberId2;

    @BeforeEach
    void init() {
        Member member = new Member("길동", "test1", MemberRole.USER, "test1", "test1");
        memberId = memberService.save(member);

        Member member2 = new Member("홍길", "test2", MemberRole.USER, "test2", "test2");
        memberId2 = memberService.save(member2);
    }

    private Long createExercise(String name, String memo, List<RepWeightDto> repWeightDtos, Classification classification, Long memberId) {
        ExerciseCreateDto exerciseCreateDto = new ExerciseCreateDto(name, memo, repWeightDtos, classification);
        return exerciseBusinessService.createExercise(memberId, exerciseCreateDto);
    }

    @Test
    @DisplayName("운동 생성")
    void create() {
        RepWeightDto repWeightDto = new RepWeightDto(50, 5);
        RepWeightDto repWeightDto1 = new RepWeightDto(50, 10);
        RepWeightDto repWeightDto2 = new RepWeightDto(50, 20);

        List<RepWeightDto> repWeightDtos = List.of(new RepWeightDto[]{repWeightDto, repWeightDto1, repWeightDto2});

        Long id = createExercise("스쿼트", "어깨넓이로", repWeightDtos, Classification.LOWERBODY, memberId);

        em.clear();

        Exercise exercise = exerciseDataService.fetchById(id);
        List<RepWeight> repWeightList = exercise.getRepWeightList();

        assertThat(exercise.getName()).isEqualTo("스쿼트");
        assertThat(repWeightList)
                .extracting(rw -> rw.getWeight())
                .contains(5.0, 10.0, 20.0);
    }



    @Test
    @DisplayName("운동 업데이트")
    void update() {
        RepWeightDto repWeightDto = new RepWeightDto(50, 5);
        RepWeightDto repWeightDto1 = new RepWeightDto(50, 10);
        RepWeightDto repWeightDto2 = new RepWeightDto(50, 20);

        List<RepWeightDto> repWeightDtos = List.of(new RepWeightDto[]{repWeightDto, repWeightDto1, repWeightDto2});

        Long exerciseId = createExercise("스쿼트", "어깨넓이로", repWeightDtos, Classification.LOWERBODY, memberId);

        ExerciseUpdateDto exerciseUpdateDto = new ExerciseUpdateDto();
        exerciseUpdateDto.setName("푸쉬업");
        exerciseUpdateDto.setMemo("");
        exerciseUpdateDto.setClassification(Classification.CHEST);

        RepWeightDto repWeightDto3 = new RepWeightDto(30, 0);
        RepWeightDto repWeightDto4 = new RepWeightDto(20, 0);
        RepWeightDto repWeightDto5 = new RepWeightDto(10, 0);

        List<RepWeightDto> repWeightDtosPushUp = List.of(new RepWeightDto[]{repWeightDto3, repWeightDto4, repWeightDto5});

        exerciseUpdateDto.setRepWeightListDto(repWeightDtosPushUp);
        exerciseBusinessService.update(exerciseId, exerciseUpdateDto);

        em.clear();

        Exercise exercise = exerciseDataService.fetchById(exerciseId);

        assertThat(exercise.getName()).isEqualTo("푸쉬업");
        assertThat(exercise.getMemo()).isEqualTo("");
        assertThat(exercise.getClassification()).isEqualTo(Classification.CHEST);

        assertThat(exercise.getRepWeightList())
                .extracting(rw -> rw.getReps())
                .contains(30, 20, 10);
    }

    @Test
    @DisplayName("운동 삭제")
    void delete() {
        RepWeightDto repWeightDto = new RepWeightDto(50, 5);
        RepWeightDto repWeightDto1 = new RepWeightDto(50, 10);
        RepWeightDto repWeightDto2 = new RepWeightDto(50, 20);

        List<RepWeightDto> repWeightDtos = List.of(new RepWeightDto[]{repWeightDto, repWeightDto1, repWeightDto2});

        Long exerciseId = createExercise("스쿼트", "어깨넓이로", repWeightDtos, Classification.LOWERBODY, memberId);
        exerciseBusinessService.delete(exerciseId);
        em.clear();

        org.junit.jupiter.api.Assertions.assertThrows(EntityNotFoundException.class, () -> exerciseDataService.findById(exerciseId));
    }

    @Test
    @DisplayName("나의 운동 조회")
    void memberExercises() {
        RepWeightDto repWeightDto = new RepWeightDto(50, 5);
        RepWeightDto repWeightDto1 = new RepWeightDto(50, 10);
        RepWeightDto repWeightDto2 = new RepWeightDto(50, 20);

        List<RepWeightDto> repWeightDtos = List.of(new RepWeightDto[]{repWeightDto, repWeightDto1, repWeightDto2});
        createExercise("스쿼트", "어깨넓이로", repWeightDtos, Classification.LOWERBODY, memberId);

        RepWeightDto repWeightDto3 = new RepWeightDto(30, 0);
        RepWeightDto repWeightDto4 = new RepWeightDto(20, 0);
        RepWeightDto repWeightDto5 = new RepWeightDto(10, 0);

        List<RepWeightDto> repWeightDtosPushUp = List.of(new RepWeightDto[]{repWeightDto3, repWeightDto4, repWeightDto5});
        createExercise("푸쉬업", "", repWeightDtosPushUp, Classification.CHEST, memberId);

        RepWeightDto repWeightDto6 = new RepWeightDto(30, 0);
        RepWeightDto repWeightDto7 = new RepWeightDto(20, 0);
        RepWeightDto repWeightDto8 = new RepWeightDto(10, 0);

        List<RepWeightDto> repWeightDtosPikePushUp = List.of(new RepWeightDto[]{repWeightDto6, repWeightDto7, repWeightDto8});
        createExercise("파이크 푸쉬업", "", repWeightDtosPikePushUp, Classification.CHEST, memberId2);

        em.clear();

        List<ExerciseResponseDto> exerciseResponseDtoList = exerciseBusinessService.fetchMemberExercises(memberId);

        assertThat(exerciseResponseDtoList)
                .hasSize(2)
                .extracting(ex -> ex.getName())
                .contains("스쿼트", "푸쉬업");

    }

    @Test
    @DisplayName("운동의 RepWeight까지 가져오기")
    void fetchOne() {
        RepWeightDto repWeightDto = new RepWeightDto(50, 5);
        RepWeightDto repWeightDto1 = new RepWeightDto(50, 10);
        RepWeightDto repWeightDto2 = new RepWeightDto(50, 20);

        List<RepWeightDto> repWeightDtos = List.of(new RepWeightDto[]{repWeightDto, repWeightDto1, repWeightDto2});
        Long exerciseId = createExercise("스쿼트", "어깨넓이로", repWeightDtos, Classification.LOWERBODY, memberId);

        Exercise exercise = exerciseDataService.fetchById(exerciseId);
        List<RepWeight> repWeightList = exercise.getRepWeightList();

        em.clear();

        ExerciseResponseDto exerciseResponseDto = exerciseBusinessService.fetchOne(exerciseId);
        assertThat(exerciseResponseDto.getName()).isEqualTo("스쿼트");
        assertThat(exerciseResponseDto.getRepWeightList())
                .extracting(rw -> rw.getWeight())
                .contains(5.0, 10.0, 20.0);
    }

    @Test
    @DisplayName("모든 운동 가져오기")
    void fetchAll() {
        RepWeightDto repWeightDto = new RepWeightDto(50, 5);
        RepWeightDto repWeightDto1 = new RepWeightDto(50, 10);
        RepWeightDto repWeightDto2 = new RepWeightDto(50, 20);

        List<RepWeightDto> repWeightDtos = List.of(new RepWeightDto[]{repWeightDto, repWeightDto1, repWeightDto2});
        createExercise("스쿼트", "어깨넓이로", repWeightDtos, Classification.LOWERBODY, memberId);

        RepWeightDto repWeightDto3 = new RepWeightDto(30, 0);
        RepWeightDto repWeightDto4 = new RepWeightDto(20, 0);
        RepWeightDto repWeightDto5 = new RepWeightDto(10, 0);

        List<RepWeightDto> repWeightDtosPushUp = List.of(new RepWeightDto[]{repWeightDto3, repWeightDto4, repWeightDto5});
        createExercise("푸쉬업", "", repWeightDtosPushUp, Classification.CHEST, memberId);

        RepWeightDto repWeightDto6 = new RepWeightDto(30, 0);
        RepWeightDto repWeightDto7 = new RepWeightDto(20, 0);
        RepWeightDto repWeightDto8 = new RepWeightDto(10, 0);

        List<RepWeightDto> repWeightDtosPikePushUp = List.of(new RepWeightDto[]{repWeightDto6, repWeightDto7, repWeightDto8});
        createExercise("파이크 푸쉬업", "", repWeightDtosPikePushUp, Classification.CHEST, memberId2);

        em.clear();

        List<ExerciseResponseDto> exerciseResponseDtos = exerciseBusinessService.fetchAll();

        assertThat(exerciseResponseDtos)
                .hasSize(3)
                .extracting(ex -> ex.getName())
                .contains("스쿼트", "푸쉬업", "파이크 푸쉬업");
    }

}