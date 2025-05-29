package com.example.everyhealth.repository;

import com.example.everyhealth.domain.TodayExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodayExerciseRepository extends JpaRepository<TodayExercise, Long> {

    @Query("select te " +
            "from TodayExercise te " +
            "join fetch te.exercise e " +
            "join fetch te.repWeightList " +
            "where te.today.id=:todayId " +
            "order by te.sequence asc ")
    List<TodayExercise> fetchByTodayId(@Param("todayId") Long todayId);

    @Query("select te " +
            "from TodayExercise te " +
            "join fetch te.exercise e " +
            "join fetch te.repWeightList " +
            "where te.today.id in :todayIds " +
            "order by te.sequence asc ")
    List<TodayExercise> fetchByTodayIdIn(@Param("todayIds") List<Long> todayIds);

    @Query("select te from TodayExercise te where te.today.id=:todayId")
    List<TodayExercise> findByTodayId(@Param("todayId") Long todayId);

    @Modifying
    @Query("delete from TodayExercise te where te.id =:todayExerciseId")
    void deleteById(@Param("todayExerciseId") Long todayExerciseId);
}
