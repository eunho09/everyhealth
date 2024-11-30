package com.example.everyhealth.dto;

import lombok.Data;

@Data
public class PostDto {

    private Long id;
    private String text;
    private String imageUrl;

    public PostDto(Long id, String text, String imageUrl) {
        this.id = id;
        this.text = text;
        this.imageUrl = imageUrl;
    }
}
