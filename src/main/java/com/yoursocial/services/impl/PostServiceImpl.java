package com.yoursocial.services.impl;

import com.yoursocial.dto.PostRequest;
import com.yoursocial.dto.PostResponse;
import com.yoursocial.dto.PostResponse.CommentResponse;
import com.yoursocial.dto.PostResponse.CommentUserDetails;
import com.yoursocial.entity.Post;
import com.yoursocial.entity.User;
import com.yoursocial.exception.DuplicateLikeException;
import com.yoursocial.exception.ResourceNotFoundException;
import com.yoursocial.repository.PostRepository;
import com.yoursocial.repository.UserRepository;
import com.yoursocial.services.PostService;
import com.yoursocial.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

        return getPostResponse(post);
    }

    @Override
    public List<PostResponse> allPost() {

        List<Post> all = postRepository
                .findAll();


        return all.stream().map(PostServiceImpl::getPostResponse).toList();
    }


    @Override
    public boolean likedPost(Integer postId) {
        // Get current user
        User user = util.getLoggedInUserDetails();


        // Find post
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        // Initialize like list if null
        if (post.getLike() == null) {
            post.setLike(new ArrayList<>());
        }

        List<User> likes = post.getLike();
        boolean isLiked;

        // Check if user already liked the post
        boolean alreadyLiked = likes.stream()
                .anyMatch(u -> u.getId().equals(user.getId()));

        if (alreadyLiked) {
            // Unlike the post
            likes.removeIf(u -> u.getId().equals(user.getId()));
            isLiked = false;
        } else {
            // Like the post
            likes.add(user);
            isLiked = true;
        }

        try {
            postRepository.save(post);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateLikeException("User already liked this post");
        }

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

    private static PostResponse getPostResponse(Post post) {

        return PostResponse.builder()
                .postId(post.getId())
                .image(post.getImage())
                .video(post.getVideo())
                .caption(post.getCaption())
                .authorId(post.getUser().getId())
                .authorEmail(post.getUser().getEmail())
                .createdAt(post.getCreatedAt())
                .likedByUserIds(post.getLike().stream().map(User::getId).toList())
                .comments(post.getComments().stream().map(comment -> CommentResponse.builder()
                        .commentId(comment.getId())
                        .content(comment.getContent())
                        .user(CommentUserDetails.builder()
                                .id(post.getUser().getId())
                                .firstName(post.getUser().getFirstName())
                                .email(post.getUser().getEmail())
                                .build())
                        .commentLikes(post.getLike().stream().map(User::getId).toList())
                        .build()).toList())
                .build();
    }


}
