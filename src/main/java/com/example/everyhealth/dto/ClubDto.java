package com.example.everyhealth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@AllArgsConstructor
public class ClubDto {

    private String title;
    private String content;
    private String location;
    private String schedule;
    private List<String> highlights = new ArrayList<>();
    private Long chatRoomId;
}
