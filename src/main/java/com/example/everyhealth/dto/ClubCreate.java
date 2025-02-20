package com.example.everyhealth.dto;

import jakarta.persistence.ElementCollection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@AllArgsConstructor
public class ClubCreate {

    private String title;
    private String content;
    private String location;
    private String schedule;
    private List<String> highlights = new ArrayList<>();
}
