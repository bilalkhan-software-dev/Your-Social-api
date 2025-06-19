package com.yoursocial.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    private UserResponse userDetails;
    private String token;

}
