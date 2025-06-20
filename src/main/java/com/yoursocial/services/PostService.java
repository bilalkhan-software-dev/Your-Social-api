package com.yoursocial.services;

import com.yoursocial.dto.PostRequest;
import com.yoursocial.dto.PostResponse;

import java.util.List;

public interface PostService {

    boolean createPost(PostRequest postRequest);

    void deletePost(Integer postId);

    PostResponse findPostById(Integer postId);

    List<PostResponse> allPost();

    boolean savedPost(Integer postId);

    boolean likedPost(Integer postId);

}
