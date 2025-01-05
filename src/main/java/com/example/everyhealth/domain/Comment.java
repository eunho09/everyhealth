package com.example.everyhealth.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Comment {

    @Id @GeneratedValue
    private Long id;

    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private LocalDateTime localDateTime;

    public Comment(String text, Post post, Member member) {
        this.text = text;
        this.post = post;
        post.getCommentList().add(this);
        this.member = member;
        member.getCommentList().add(this);
        this.localDateTime = LocalDateTime.now();
    }

    protected Comment() {
    }

    public void setPost(Post post) {
        this.post = post;
        post.getCommentList().add(this);
    }
}
