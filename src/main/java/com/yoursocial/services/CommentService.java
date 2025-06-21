package com.yoursocial.services;

import com.yoursocial.dto.CommentRequest;
import com.yoursocial.dto.CommentResponse;

import java.util.List;

public interface CommentService {

    boolean createComment(CommentRequest commentRequest, Integer postId);

    CommentResponse findCommentById(Integer commentId);

    List<CommentResponse> getAllCommentsOfThePost(Integer postId);

    List<CommentResponse> allComments();

    boolean likeComment(Integer commentId);

}
