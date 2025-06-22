package com.yoursocial.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
public class MessageResponse {

    private Integer messageId;

    private String content;

    private String image;

    private LocalDateTime messageCreatedAt;

    private MessageUserDetails messageUserDetails;

    private ChatResponse chatDetails;


    @Data
    @Builder
    public static class MessageUserDetails {

        private Integer userId;
        private String fullName;
        private String email;


    }


}
