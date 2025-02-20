package com.example.everyhealth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberChatResponseDto {

    private Long id;
    private String name;
    private String picture;
}
