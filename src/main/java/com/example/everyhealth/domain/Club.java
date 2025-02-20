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

    @OneToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    @ElementCollection
    private List<String> highlights = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    protected Club() {
    }

    public Club(String title, String content, Member member, String location, String schedule, List<String> highlights) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.location = location;
        this.schedule = schedule;
        this.highlights = highlights;
    }

    public void setMember(Member member) {
        this.member = member;
        member.getClubList().add(this);
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }
}
