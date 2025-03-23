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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private ChatRoom chatRoom;

    @ElementCollection
    private List<String> highlights = new ArrayList<>();

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClubMember> clubMemberList = new ArrayList<>();

    protected Club() {
    }

    public Club(String title, String content, String location, String schedule, List<String> highlights) {
        this.title = title;
        this.content = content;
        this.location = location;
        this.schedule = schedule;
        this.highlights = highlights;
    }


    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }
}
