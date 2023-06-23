package com.example.preauthorize2.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupRequestDTO {
    private  String email,password,authority;
}
