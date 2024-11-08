package com.example.everyhealth.repository;

import com.example.everyhealth.domain.Today;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodayRepository extends JpaRepository<Today, Long> {
}
