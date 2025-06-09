package com.example.everyhealth.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Club {

    @Id @GeneratedValue
    private Long id;

    private String title;
    private String content;
    private String location;
    private String schedule;

    @OneToOne(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    private ChatRoom chatRoom;

    private String highlight;

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClubMember> clubMemberList = new ArrayList<>();

    protected Club() {
    }

    public Club(String title, String content, String location, String schedule, String highlight) {
        this.title = title;
        this.content = content;
        this.location = location;
        this.schedule = schedule;
        this.highlight = highlight;
    }


    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }
}
