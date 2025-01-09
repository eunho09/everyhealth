package com.example.everyhealth.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Today {

    @Id @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "today", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TodayExercise> todayExercises = new ArrayList<>(); //운동기록 n개

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDate localDate;

    @Setter
    @Enumerated(EnumType.STRING)
    private CheckBox checkBox;

    private int recodeTime;

    public Today(LocalDate localDate, Member member) {
        this.localDate = localDate;
        this.checkBox = CheckBox.False;
        this.member = member;
        member.getTodayList().add(this);
    }

    public Today() {
    }

}
