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
    private LocalDateTime createdAt;



}

