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
    private String authorFirstName;
    private String authorLastName;
    private String authorProfilePic;
    private Boolean isLiked;
    private Boolean isSaved;
    private Integer postLikesCount;
    private Integer commentsCount;
    private Integer savesCount;
    @Builder.Default
    private List<Integer> likedByUserIds = new ArrayList<>();
    @Builder.Default
    private List<CommentResponse> comments = new ArrayList<>();
    private LocalDateTime createdAt;

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class CommentResponse {
        private Integer commentId;
        private String content;
        private CommentUserDetails commentUserDetails;
        @Builder.Default
        private List<Integer> commentLikes = new ArrayList<>();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class CommentUserDetails {
        private Integer id;
        private String firstName;
        private String email;
    }
}