package com.example.everyhealth.dto;

import com.example.everyhealth.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class MemberDto {

    private Long id;
    private String name;
    private String picture;

    public MemberDto(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.picture = member.getPicture();
    }
}
