package com.example.everyhealth.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Comments {

    @Id @GeneratedValue
    private Long id;

    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
}
