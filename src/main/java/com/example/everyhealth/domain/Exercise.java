package com.example.everyhealth.domain;

import com.example.everyhealth.dto.ExerciseDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Exercise {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String memo;

    //[0][0] repetition [0][1] weight
    @ElementCollection
    private List<ArrayList<Integer>> repWeight = new ArrayList<>();

    @OneToMany(mappedBy = "exercise")
    private List<TodayExercise> todayExercises = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Classification classification; //분류

    @OneToMany(mappedBy = "exercise")
    private List<RoutineExercise> routineExerciseList = new ArrayList<>();

    public Exercise(String name, Member member, String memo, List<ArrayList<Integer>> repWeight, Classification classification) {
        this.name = name;
        setMember(member);
        this.memo = memo;
        this.repWeight = repWeight;
        this.classification = classification;
    }

    protected Exercise() {
    }

    public void update(ExerciseDto dto) {
        this.name = dto.getName();
        this.memo = dto.getMemo();
        this.repWeight = dto.getRepWeight();
        this.classification = dto.getClassification();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setRepWeight(List<ArrayList<Integer>> repWeight) {
        this.repWeight = repWeight;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }

    public void setMember(Member member) {
        this.member = member;
        member.getExerciseList().add(this);
    }

}
