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
public class Post {

    @Id @GeneratedValue
    private Long id;

    private String text;
    private String imageUrl;

    @OneToMany(mappedBy = "post")
    private List<Comment> commentList = new ArrayList<>();


    public Post(String text, String imageUrl) {
        this.text = text;
        this.imageUrl = imageUrl;
    }

    protected Post() {

    }
}
