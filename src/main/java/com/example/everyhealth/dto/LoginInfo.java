package com.example.everyhealth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class LoginInfo {

    private Long id;
    private String name;
    private String picture;
    private Long expiresIn;
    private boolean check;

}
