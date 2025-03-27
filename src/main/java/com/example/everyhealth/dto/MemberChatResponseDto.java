package com.example.everyhealth.dto;

import com.example.everyhealth.domain.ClubMember;
import com.example.everyhealth.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class MemberChatResponseDto {

    private Long id;
    private String name;
    private String picture;

    public MemberChatResponseDto(ClubMember clubMember) {
        this.id = clubMember.getMember().getId();
        this.name = clubMember.getMember().getName();
        this.picture = clubMember.getMember().getPicture();
    }
}
