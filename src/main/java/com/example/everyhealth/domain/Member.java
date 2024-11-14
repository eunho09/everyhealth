package com.example.everyhealth.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String name;
    private String loginId;
    private String password;

    @OneToMany(mappedBy = "member")
    private List<Routine> routineList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Exercise> exerciseList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Friend> friendList = new ArrayList<>();

    public Member(String name, String loginId, String password) {
        this.name = name;
        this.loginId = loginId;
        this.password = password;
    }

    protected Member() {
    }

    public void setName(String name) {
        this.name = name;
    }
}
