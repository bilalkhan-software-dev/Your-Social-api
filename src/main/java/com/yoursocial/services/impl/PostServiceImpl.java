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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final CommonUtil util;

    @Override
    @Transactional
    public PostResponse createPost(PostRequest postRequest) {
        Integer userId = util.getLoggedInUserDetails().getId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Post post = mapper.map(postRequest, Post.class);
        post.setCreatedAt(LocalDateTime.now());
        post.setUser(user);

        Post savedPost = postRepository.save(post);
        return getPostResponse(savedPost);
    }

    @Override
    public void deletePost(Integer postId) {
        Integer userId = util.getLoggedInUserDetails().getId();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        if (!post.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("You can't delete another user's post");
        }
        postRepository.delete(post);
    }

    @Override
    public PostResponse findPostById(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        return getPostResponse(post);
    }

    @Override
    public List<PostResponse> allPost() {
        return postRepository.findAll().stream()
                .map(this::getPostResponse)
                .toList();
    }

    @Override
    public List<PostResponse> getAllUserPost() {

        User loggedInUser = util.getLoggedInUserDetails();

        List<Post> loggedInUserPosts = postRepository.findByUser(loggedInUser);
        return loggedInUserPosts.stream().map(this::getPostResponse).toList();
    }

    public PostResponse likedPost(Integer postId) {
        User user = util.getLoggedInUserDetails();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        // Initialize like list if null
        if (post.getLike() == null) {
            post.setLike(new ArrayList<>());
        }

        List<User> likes = post.getLike();
        boolean isAlreadyLiked = likes.stream().anyMatch(u -> u.getId().equals(user.getId()));

        if (isAlreadyLiked) {
            // Unlike the post
            likes.removeIf(u -> u.getId().equals(user.getId()));
        } else {
            // Like the post
            likes.add(user);
        }

        try {
            Post updatedPost = postRepository.save(post);
            PostResponse response = getPostResponse(updatedPost);
            response.setIsLiked(!isAlreadyLiked); // Set the opposite of previous state
            return response;
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateLikeException("Error processing like action");
        }
    }

    @Override
    @Transactional
    public PostResponse savedPost(Integer postId) {
        Integer userId = util.getLoggedInUserDetails().getId();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        boolean isCurrentlySaved = user.getSavedPost().contains(post);
        if (isCurrentlySaved) {
            user.getSavedPost().remove(post);
        } else {
            user.getSavedPost().add(post);
        }

        userRepository.save(user);
        PostResponse response = getPostResponse(post);
        response.setIsSaved(!isCurrentlySaved);
        return response;
    }


    private PostResponse getPostResponse(Post post) {
        User author = post.getUser();

        return PostResponse.builder()
                .postId(post.getId())
                .caption(post.getCaption())
                .image(post.getImage())
                .createdAt(post.getCreatedAt())
                .video(post.getVideo())
                .authorId(author.getId())
                .authorEmail(author.getEmail())
                .authorFirstName(author.getFirstName())
                .authorLastName(author.getLastName())
                .authorProfilePic(author.getImage())
                .postLikesCount(post.getLike().size())
                .commentsCount(post.getComments().size())
                .savesCount(author.getSavedPost().size())
                .likedByUserIds(post.getLike().stream().map(User::getId).toList())
                .comments(post.getComments().stream().map(comment ->
                        CommentResponse.builder()
                                .commentId(comment.getId())
                                .content(comment.getContent())
                                .commentUserDetails(CommentUserDetails.builder()
                                        .id(author.getId())
                                        .firstName(author.getFirstName())
                                        .email(author.getEmail())
                                        .build())
                                .commentLikes(comment.getLiked().stream().map(User::getId).toList())
                                .build()).toList()
                )
                .build();
    }
}
