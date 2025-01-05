package com.example.everyhealth.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Member {

    @Id @GeneratedValue
    private Long id;

    @Setter
    private String name;
    private String loginId;
    private String picture;

    @Enumerated(EnumType.STRING)
    private MemberRole role;
    private String providerId;
    private String password;

    @OneToMany(mappedBy = "member")
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Routine> routineList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Exercise> exerciseList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Friend> friendList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Today> todayList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Post> postList = new ArrayList<>();

    public Member(String name, String loginId, MemberRole role, String providerId, String picture) {
        this.name = name;
        this.loginId = loginId;
        this.role = role;
        this.providerId = providerId;
        this.picture = picture;
    }

    protected Member() {
    }

}
