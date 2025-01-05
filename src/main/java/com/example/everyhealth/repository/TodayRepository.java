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

    @Query("select new com.example.everyhealth.dto.TodayDateDto(t.localDate) from Today t where month(t.localDate)=:month and t.member.id=:memberId ")
    List<TodayDateDto> findByMonth(@Param("month") int month, @Param("memberId") Long memberId);

    @Query("select t from Today t left join fetch t.todayExercises te left join fetch te.exercise where date(t.localDate)=:date and t.member.id=:memberId order by te.sequence asc")
    Today fetchByLocalDate(@Param("date") LocalDate date, @Param("memberId") Long memberId);

    @Query("select t from Today t join fetch t.todayExercises te where t.id=:todayId")
    Today fetchById(@Param("todayId") Long todayId);
}

