package com.example.everyhealth.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Post {

    @Id @GeneratedValue
    private Long id;

    private String text;
    private String imageUrl;
    private LocalDateTime createAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    public Post(String text, String imageUrl, Member member) {
        this.text = text;
        this.imageUrl = imageUrl;
        this.member = member;
        member.getPostList().add(this);
        this.createAt = LocalDateTime.now();
    }

    protected Post() {

    }
}
