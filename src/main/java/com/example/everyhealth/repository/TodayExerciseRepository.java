package com.example.everyhealth.repository;

import com.example.everyhealth.domain.TodayExercise;
import com.example.everyhealth.dto.TodayExerciseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodayExerciseRepository extends JpaRepository<TodayExercise, Long> {

    @Query("select te " +
            "from TodayExercise te " +
            "join fetch te.exercise e " +
            "join fetch te.repWeight " +
            "where te.today.id=:todayId " +
            "order by te.sequence asc ")
    List<TodayExercise> findByTodayId(@Param("todayId") Long todayId);

}
