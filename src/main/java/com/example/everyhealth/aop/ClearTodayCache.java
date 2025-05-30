package com.example.everyhealth.aop;

import org.springframework.cache.annotation.CacheEvict;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@CacheEvict(value = {"todays", "todayByLocalDate", "todayByYearAndMonth"}, allEntries = true)
public @interface ClearTodayCache {
}
