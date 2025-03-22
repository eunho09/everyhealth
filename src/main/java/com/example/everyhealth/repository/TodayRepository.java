package com.example.everyhealth.repository;

import com.example.everyhealth.domain.Today;
import com.example.everyhealth.dto.TodayDateDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TodayRepository extends JpaRepository<Today, Long> {

    @Query("select new com.example.everyhealth.dto.TodayDateDto(t.localDate, t.checkBox) from Today t where year(t.localDate)=:year and month(t.localDate)=:month and t.member.id=:memberId ")
    List<TodayDateDto> findByYearAndMonth(@Param("year") int year, @Param("month") int month, @Param("memberId") Long memberId);

    @Query("select t from Today t where date(t.localDate)=:date and t.member.id=:memberId")
    Today findByLocalDateAndMemberId(@Param("date") LocalDate date, @Param("memberId") Long memberId);

    @Query("select t from Today t left join fetch t.todayExercises te where date(t.localDate)=:date and t.member.id=:memberId order by te.sequence asc")
    Today fetchWithTodayExercises(@Param("date") LocalDate date, @Param("memberId") Long memberId);

    @Query("select t from Today t join fetch t.todayExercises te where t.id=:todayId")
    Today fetchByIdWithTodayExercises(@Param("todayId") Long todayId);

    @Query("select t from Today t join fetch t.todayExercises te where t.member.id=:memberId")
    List<Today> fetchByMemberId(@Param("memberId") Long memberId);
}

