package com.example.everyhealth.service;

import com.example.everyhealth.domain.*;
import com.example.everyhealth.dto.*;
import com.example.everyhealth.exception.TodayException;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@TestPropertySource(locations = "file:C:/coding/spring/project/everyhealth/src/main/resources/.env-local")
class TodayBusinessServiceTest {

    @Autowired
    TodayBusinessService todayBusinessService;
    @Autowired
    TodayDataService todayDataService;
    @Autowired
    ExerciseBusinessService exerciseBusinessService;
    @Autowired
    MemberService memberService;
    @Autowired
    EntityManager em;

    private Long memberId;
    private Long memberId2;

    private Long createExercise(String name, String memo, List<RepWeightDto> repWeightDtos, Classification classification, Long memberId) {
        ExerciseCreateDto exerciseCreateDto = new ExerciseCreateDto(name, memo, repWeightDtos, classification);
        return exerciseBusinessService.createExercise(memberId, exerciseCreateDto);
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
    @DisplayName("오늘 생성")
    void create() {
        Long todayId = todayBusinessService.createToday(memberId, LocalDate.now());

        em.clear();
        Today today = todayDataService.findById(todayId);
        Assertions.assertThat(today.getId()).isEqualTo(todayId);
    }

    @Test
    @DisplayName("오늘 날짜 중복 예외")
    void createException() {
        Long todayId = todayBusinessService.createToday(memberId, LocalDate.of(2025, 5, 10));

        org.junit.jupiter.api.Assertions.assertThrows(TodayException.class, () -> todayBusinessService.createToday(memberId, LocalDate.of(2025, 5, 10)));
    }

    @Test
    @DisplayName("오늘 체크리스트 업데이트")
    void todayCheckBox() {
        Long todayId = todayBusinessService.createToday(memberId, LocalDate.now());
        todayBusinessService.updateCheckbox(true, todayId);

        em.clear();

        Today today = todayDataService.findById(todayId);
        Assertions.assertThat(today.getCheckBox()).isEqualTo(CheckBox.True);
    }

    @Test
    @DisplayName("오늘 운동 추가")
    void addTodayExercise() {
        Long todayId = todayBusinessService.createToday(memberId, LocalDate.of(2025, 6, 10));


        RepWeightDto repWeightDto = new RepWeightDto(50, 5);
        RepWeightDto repWeightDto1 = new RepWeightDto(50, 10);
        RepWeightDto repWeightDto2 = new RepWeightDto(50, 20);

        List<RepWeightDto> repWeightDtos = List.of(new RepWeightDto[]{repWeightDto, repWeightDto1, repWeightDto2});

        Long id = createExercise("스쿼트", "어깨넓이로", repWeightDtos, Classification.LOWERBODY, memberId);

        RepWeightDto repWeightDto3 = new RepWeightDto(30, 0);
        RepWeightDto repWeightDto4 = new RepWeightDto(20, 0);
        RepWeightDto repWeightDto5 = new RepWeightDto(10, 0);

        List<RepWeightDto> repWeightDtosPushUp = List.of(new RepWeightDto[]{repWeightDto3, repWeightDto4, repWeightDto5});
        Long id2 = createExercise("푸쉬업", "", repWeightDtosPushUp, Classification.CHEST, memberId);

        List<TodayExerciseRequest> todayExerciseRequests = List.of(new TodayExerciseRequest(id, "exercise", 1), new TodayExerciseRequest(id2, "exercise", 2));
        todayBusinessService.addTodayExercise(todayExerciseRequests, LocalDate.of(2025, 6, 10), memberId);

        em.clear();
        TodayDto todayDto = todayBusinessService.fetchById(todayId);

        Assertions.assertThat(todayDto.getTodayExercises())
                .hasSize(2)
                .extracting(t -> t.getExerciseName())
                .contains("스쿼트", "푸쉬업");
    }

    @Test
    @DisplayName("오늘 운동 순서")
    void addTodayExerciseSequence() {
        Long todayId = todayBusinessService.createToday(memberId, LocalDate.of(2025, 6, 10));


        RepWeightDto repWeightDto = new RepWeightDto(50, 5);
        RepWeightDto repWeightDto1 = new RepWeightDto(50, 10);
        RepWeightDto repWeightDto2 = new RepWeightDto(50, 20);

        List<RepWeightDto> repWeightDtos = List.of(new RepWeightDto[]{repWeightDto, repWeightDto1, repWeightDto2});

        Long id = createExercise("스쿼트", "어깨넓이로", repWeightDtos, Classification.LOWERBODY, memberId);

        RepWeightDto repWeightDto3 = new RepWeightDto(30, 0);
        RepWeightDto repWeightDto4 = new RepWeightDto(20, 0);
        RepWeightDto repWeightDto5 = new RepWeightDto(10, 0);

        List<RepWeightDto> repWeightDtosPushUp = List.of(new RepWeightDto[]{repWeightDto3, repWeightDto4, repWeightDto5});
        Long id2 = createExercise("푸쉬업", "", repWeightDtosPushUp, Classification.CHEST, memberId);

        List<TodayExerciseRequest> todayExerciseRequests = List.of(new TodayExerciseRequest(id, "exercise", 3), new TodayExerciseRequest(id2, "exercise", 3));
        todayBusinessService.addTodayExercise(todayExerciseRequests, LocalDate.of(2025, 6, 10), memberId);

        em.clear();
        TodayDto todayDto = todayBusinessService.fetchById(todayId);

        Assertions.assertThat(todayDto.getTodayExercises())
                .hasSize(2)
                .extracting(t -> t.getSequence())
                .contains(1, 2);
    }

    @Test
    @DisplayName("오늘 운동 업데이트")
    void updateTodayExercise() {
        Long todayId = todayBusinessService.createToday(memberId, LocalDate.of(2025, 6, 10));

        RepWeightDto repWeightDto = new RepWeightDto(50, 5);
        RepWeightDto repWeightDto1 = new RepWeightDto(50, 10);
        RepWeightDto repWeightDto2 = new RepWeightDto(50, 20);

        List<RepWeightDto> repWeightDtos = List.of(new RepWeightDto[]{repWeightDto, repWeightDto1, repWeightDto2});

        Long id = createExercise("스쿼트", "어깨넓이로", repWeightDtos, Classification.LOWERBODY, memberId);

        RepWeightDto repWeightDto3 = new RepWeightDto(30, 0);
        RepWeightDto repWeightDto4 = new RepWeightDto(20, 0);
        RepWeightDto repWeightDto5 = new RepWeightDto(10, 0);

        List<RepWeightDto> repWeightDtosPushUp = List.of(new RepWeightDto[]{repWeightDto3, repWeightDto4, repWeightDto5});
        Long id2 = createExercise("푸쉬업", "", repWeightDtosPushUp, Classification.CHEST, memberId);

        List<TodayExerciseRequest> todayExerciseRequests = List.of(new TodayExerciseRequest(id, "exercise", 1), new TodayExerciseRequest(id2, "exercise", 2));
        todayBusinessService.addTodayExercise(todayExerciseRequests, LocalDate.of(2025, 6, 10), memberId);

        List<RepWeightDto> repWeightDtosPushUp2 = List.of(new RepWeightDto(20, 0.0), new RepWeightDto(15, 0.0), new RepWeightDto(10, 0.0));
        List<UpdateTodayExerciseDto> updateTodayExerciseDtos = List.of(new UpdateTodayExerciseDto(id2, repWeightDtosPushUp2));

        todayBusinessService.updateTodayExercise(updateTodayExerciseDtos, todayId);
        em.clear();
        TodayDto todayDto = todayBusinessService.fetchById(todayId);

        Assertions.assertThat(todayDto.getTodayExercises())
                .hasSize(2)
                .satisfies(tx -> {
                    Assertions.assertThat(tx.get(0).getRepWeightList())
                            .extracting(rw -> rw.getReps())
                            .containsExactly(50, 50, 50);

                    Assertions.assertThat(tx.get(1).getRepWeightList())
                            .extracting(rw -> rw.getReps())
                            .containsExactly(20, 15, 10);
                });
    }

    @Test
    @DisplayName("오늘 운동 순서 업데이트")
    void updateTodayExerciseSequence() {
        Long todayId = todayBusinessService.createToday(memberId, LocalDate.of(2025, 6, 10));


        RepWeightDto repWeightDto = new RepWeightDto(50, 5);
        RepWeightDto repWeightDto1 = new RepWeightDto(50, 10);
        RepWeightDto repWeightDto2 = new RepWeightDto(50, 20);

        List<RepWeightDto> repWeightDtos = List.of(new RepWeightDto[]{repWeightDto, repWeightDto1, repWeightDto2});

        Long id = createExercise("스쿼트", "어깨넓이로", repWeightDtos, Classification.LOWERBODY, memberId);

        RepWeightDto repWeightDto3 = new RepWeightDto(30, 0);
        RepWeightDto repWeightDto4 = new RepWeightDto(20, 0);
        RepWeightDto repWeightDto5 = new RepWeightDto(10, 0);

        List<RepWeightDto> repWeightDtosPushUp = List.of(new RepWeightDto[]{repWeightDto3, repWeightDto4, repWeightDto5});
        Long id2 = createExercise("푸쉬업", "", repWeightDtosPushUp, Classification.CHEST, memberId);

        List<TodayExerciseRequest> todayExerciseRequests = List.of(new TodayExerciseRequest(id, "exercise", 3), new TodayExerciseRequest(id2, "exercise", 3));
        todayBusinessService.addTodayExercise(todayExerciseRequests, LocalDate.of(2025, 6, 10), memberId);

        List<UpdateSeqTodayExercise> updateSeqTodayExercises = List.of(new UpdateSeqTodayExercise(id2, 1), new UpdateSeqTodayExercise(id, 2));
        todayBusinessService.updateSequence(updateSeqTodayExercises, todayId);

        em.clear();

        TodayDto todayDto = todayBusinessService.fetchById(todayId);
        Assertions.assertThat(todayDto.getTodayExercises())
                .hasSize(2)
                .satisfies(tx -> {
                    Assertions.assertThat(tx.get(0).getRepWeightList())
                            .extracting(rw -> rw.getReps())
                            .containsExactly(30, 20, 10);

                    Assertions.assertThat(tx.get(1).getRepWeightList())
                            .extracting(rw -> rw.getReps())
                            .containsExactly(50, 50, 50);
                });
    }
}
