package com.yoursocial.dto;


import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PostResponse {

    private Integer postId;
    private String caption;
    private String image;
    private String video;

    private Integer authorId;
    private String authorEmail;

    private List<Integer> likedByUserIds  = new ArrayList<>();
    private List<CommentResponse> comments = new ArrayList<>();
    private LocalDateTime createdAt;


    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class CommentResponse{

        private Integer commentId;
        private String content;
        private CommentUserDetails user;
        private List<Integer> commentLikes = new ArrayList<>();

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class CommentUserDetails{
        private Integer id;
        private String firstName;
        private String email;


    }



}

