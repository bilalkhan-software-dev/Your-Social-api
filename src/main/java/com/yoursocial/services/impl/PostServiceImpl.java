package com.yoursocial.services.impl;

import com.yoursocial.dto.PostRequest;
import com.yoursocial.dto.PostResponse;
import com.yoursocial.entity.Post;
import com.yoursocial.entity.User;
import com.yoursocial.exception.ResourceNotFoundException;
import com.yoursocial.repository.PostRepository;
import com.yoursocial.repository.UserRepository;
import com.yoursocial.services.PostService;
import com.yoursocial.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final CommonUtil util;


    @Override
    public boolean createPost(PostRequest postRequest) {

        Integer userId = util.getLoggedInUserDetails().getId();

        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("user not found ")
        );

        Post post = mapper.map(postRequest, Post.class);
        post.setCreatedAt(LocalDateTime.now());
        post.setUser(user);

        Post isPostCreated = postRepository.save(post);

        return !ObjectUtils.isEmpty(isPostCreated);
    }

    @Override
    public void deletePost(Integer postId) {

        // Get current user ID
        Integer userId = util.getLoggedInUserDetails().getId();

        // Check if post exists
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post not found")
        );

        // Check if the post belongs to the authenticated user
        if (!post.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("you can't delete another user post");
        }

        postRepository.delete(post);

    }

    @Override
    public PostResponse findPostById(Integer postId) {

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post not found")
        );


        return PostResponse.builder()
                .postId(post.getId())
                .image(post.getImage())
                .video(post.getVideo())
                .caption(post.getCaption())
                .authorId(post.getUser().getId())
                .authorEmail(post.getUser().getEmail())
                .likedByUserIds(post.getLike().stream().map(User::getId).toList())
                .createdAt(post.getCreatedAt())
                .build();
    }

    @Override
    public List<PostResponse> allPost() {

        List<Post> all = postRepository
                .findAll();


        return all.stream().map(post -> PostResponse.builder()
                .postId(post.getId())
                .image(post.getImage())
                .video(post.getVideo())
                .caption(post.getCaption())
                .authorId(post.getUser().getId())
                .authorEmail(post.getUser().getEmail())
                .createdAt(post.getCreatedAt())
                .likedByUserIds(post.getLike().stream().map(User::getId).toList())
                .build()).toList();
    }


    @Override
    public boolean likedPost(Integer postId) {

        Integer userId = util.getLoggedInUserDetails().getId();

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post not found")
        );

        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("user not found")
        );


        boolean isLiked;
        if (post.getLike().contains(user)) {
            post.getLike().remove(user);
            isLiked = false;
        } else {
            post.getLike().add(user);
            isLiked = true;
        }

        postRepository.save(post);
        return isLiked;
    }

    @Override
    public boolean savedPost(Integer postId) {

        Integer userId = util.getLoggedInUserDetails().getId();

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post not found")
        );

        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("user not found")
        );

        boolean isSavedPost;
        if (user.getSavedPost().contains(post)) {
            user.getSavedPost().remove(post);
            isSavedPost = false;
        } else {
            user.getSavedPost().add(post);
            isSavedPost = true;
        }

        userRepository.save(user);

        return isSavedPost;
    }
}
