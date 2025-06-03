package com.example.everyhealth.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Exercise {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String memo;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RepWeight> repWeightList = new ArrayList<>();

    @OneToMany(mappedBy = "exercise")
    private List<TodayExercise> todayExercises = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Classification classification; //분류

    @OneToMany(mappedBy = "exercise")
    private List<RoutineExercise> routineExerciseList = new ArrayList<>();

    public Exercise(String name, Member member, String memo, Classification classification) {
        this.name = name;
        setMember(member);
        this.memo = memo;
        this.classification = classification;
    }

    protected Exercise() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }

    public void setMember(Member member) {
        this.member = member;
        member.getExerciseList().add(this);
    }

    public void setRepWeightList(List<RepWeight> repWeightList) {
        this.getRepWeightList().clear();

        for (RepWeight repWeight : repWeightList) {
            repWeight.setExercise(this);
        }
        this.repWeightList = repWeightList;
    }

}
