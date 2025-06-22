package com.yoursocial.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ChatResponse {

    private Integer id;
    private String chatName;
    private String chatImage;
    private LocalDateTime chatCreatAt;

    private List<UserResponse> users = new ArrayList<>();


    @Data
    @Builder
    public static class UserResponse {

        private Integer userId;
        private String fullName;
        private String email;

    }


}
