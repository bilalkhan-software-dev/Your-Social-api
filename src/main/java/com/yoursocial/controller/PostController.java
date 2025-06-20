package com.yoursocial.controller;

import com.yoursocial.dto.PostRequest;
import com.yoursocial.dto.PostResponse;
import com.yoursocial.endpoint.PostControllerEndpoint;
import com.yoursocial.services.PostService;
import com.yoursocial.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class PostController implements PostControllerEndpoint {

    private final PostService postService;
    private final CommonUtil response;

    @Override
    public ResponseEntity<?> createPost(PostRequest postRequest) {


        boolean isPostCreated = postService.createPost(postRequest);

        if (isPostCreated) {
            return response.createBuildResponseMessage("post created successfully!", HttpStatus.CREATED);
        }

        return response.createErrorResponseMessage("post creation failed", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<?> allPost() {

        List<PostResponse> allPost = postService.allPost();

        if (CollectionUtils.isEmpty(allPost)) {
            return ResponseEntity.noContent().build();
        }

        return response.createBuildResponse(allPost, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> findPostById(Integer postId) {

        PostResponse postDetails = postService.findPostById(postId);

        if (!ObjectUtils.isEmpty(postDetails)) {
            return response.createBuildResponse(postDetails, HttpStatus.OK);
        }

        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity<?> deletePost(Integer postId) {

        postService.deletePost(postId);

        return response.createBuildResponseMessage("post deleted successfully!", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> likePost(Integer postId) {

        boolean isPostLiked = postService.likedPost(postId);

        if (isPostLiked) {
            return response.createBuildResponseMessage("post like successfully! if you like again then it will be removed from like post list", HttpStatus.ACCEPTED);
        }

        return response.createErrorResponseMessage("post dislike successfully!", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<?> savePost(Integer postId) {

        boolean isPostSaved = postService.savedPost(postId);
        if (isPostSaved) {
            return response.createBuildResponseMessage("post saved successfully! if you saved again then it will be removed from save post list", HttpStatus.ACCEPTED);
        }


        return response.createBuildResponseMessage("post unsaved successfully!", HttpStatus.BAD_REQUEST);
    }
}
