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

/*    @ElementCollection
    private List<Integer> repetitions = new ArrayList<>();  // 각 세트의 반복 횟수 저장

    @ElementCollection
    private List<Integer> weights = new ArrayList<>();  // 각 세트의 무게 저장*/


    //[0][0] repetition [0][1] weight
    @ElementCollection
    private List<ArrayList<Integer>> repWeight = new ArrayList<>();

    @OneToMany(mappedBy = "exercise")
    private List<TodayExercise> todayExercises = new ArrayList<>();

    private String classification; //분류

    @OneToMany(mappedBy = "exercise")
    private List<RoutineExercise> routineExerciseList = new ArrayList<>();

    public Exercise(String name, Member member, String memo, List<ArrayList<Integer>> repWeight, String classification) {
        this.name = name;
        this.member = member;
        member.getExerciseList().add(this);
        this.memo = memo;
        this.repWeight = repWeight;
        this.classification = classification;
    }

    protected Exercise() {
    }

/*    public void update(ExerciseDto dto) {
        this.name = dto.getName();
        this.memo = dto.getMemo();
        this.repetitions = dto.getRepetitions();
        this.weights = dto.getWeight();
        this.classification = dto.getClassification();
    }*/

    public void setName(String name) {
        this.name = name;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setRepWeight(List<ArrayList<Integer>> repWeight) {
        this.repWeight = repWeight;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }
}
