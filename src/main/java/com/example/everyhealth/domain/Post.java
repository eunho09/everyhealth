package com.example.everyhealth.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Post {

    @Id @GeneratedValue
    private Long id;

    private String text;
    private String imageUrl;

    @OneToMany(mappedBy = "post")
    private List<Comment> commentList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    public Post(String text, String imageUrl, Member member) {
        this.text = text;
        this.imageUrl = imageUrl;
        this.member = member;
        member.getPostList().add(this);
    }

    protected Post() {

    }
}
