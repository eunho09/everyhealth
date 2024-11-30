package com.example.everyhealth.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Comment {

    @Id @GeneratedValue
    private Long id;

    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public Comment(String text, Post post) {
        this.text = text;
        this.post = post;
    }

    protected Comment() {
    }

    public void setPost(Post post) {
        this.post = post;
        post.getCommentList().add(this);
    }
}
