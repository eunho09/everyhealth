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

    @OneToMany(mappedBy = "post")
    private List<Comments> commentsList = new ArrayList<>();

}
