package com.example.everyhealth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDto {

    private Long id;
    private String text;
    private String name;
    private LocalDateTime localDateTime;
}
