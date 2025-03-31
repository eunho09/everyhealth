package com.example.everyhealth.dto;

import com.example.everyhealth.domain.Post;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostDto {

    private Long id;
    private String text;
    private String imageUrl;

    public PostDto(Post post) {
        this.id = post.getId();
        this.text = post.getText();
        this.imageUrl = post.getImageUrl();
    }
}
