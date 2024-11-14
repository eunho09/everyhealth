package com.example.everyhealth.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Routine {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "routine", cascade = CascadeType.ALL)
    private List<RoutineExercise> routineExerciseList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Routine(String name, Member member) {
        this.name = name;
        setMember(member);
    }

    public Routine() {
    }

    public void setMember(Member member) {
        this.member = member;
        member.getRoutineList().add(this);
    }
}
