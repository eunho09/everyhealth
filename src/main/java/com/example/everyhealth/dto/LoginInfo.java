package com.example.everyhealth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class LoginInfo {

    private Long id;
    private String name;
    private String picture;
    private boolean check;

}
