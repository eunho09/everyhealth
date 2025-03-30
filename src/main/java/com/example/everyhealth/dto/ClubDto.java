package com.example.everyhealth.dto;

import com.example.everyhealth.domain.Club;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ClubDto {

    private Long id;
    private String title;
    private String content;
    private String location;
    private String schedule;
    private String highlight;
    private Long chatRoomId;

    public ClubDto(Club club) {
        this.id = club.getId();
        this.title = club.getTitle();
        this.content = club.getContent();
        this.location = club.getLocation();
        this.schedule = club.getSchedule();
        this.highlight = club.getHighlight();
        this.chatRoomId = club.getChatRoom().getId();
    }
}
