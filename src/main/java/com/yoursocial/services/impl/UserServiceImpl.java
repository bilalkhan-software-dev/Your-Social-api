package com.yoursocial.services.impl;

import com.yoursocial.dto.UpdateUserRequest;
import com.yoursocial.dto.UserResponse;
import com.yoursocial.dto.UserResponse.PostResponse;
import com.yoursocial.entity.User;
import com.yoursocial.exception.ResourceNotFoundException;
import com.yoursocial.repository.UserRepository;
import com.yoursocial.services.UserService;
import com.yoursocial.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final CommonUtil util;


    @Override
    public UserResponse showUserProfile() {

        User user = util.getLoggedInUserDetails();

        return getUserResponse(user);
    }

    @Override
    public UserResponse findUserById(Integer userId) {

        User user = userRepo.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not available or created with id: " + userId)
        );

        return getUserResponse(user);
    }

    @Override
    public UserResponse findUserByEmail(String email) {

        User user = userRepo.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User not available or created with email: " + email)
        );

        return getUserResponse(user);
    }


    @Override
    public UserResponse updateUser(UpdateUserRequest userRequest) {

        Integer userId = util.getLoggedInUserDetails().getId();
        User existingUser = userRepo.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found with id:" + userId)
        );


        if (userRequest.getFirstName() != null) {
            existingUser.setFirstName(userRequest.getFirstName());
        }
        if (userRequest.getLastName() != null) {
            existingUser.setLastName(userRequest.getLastName());
        }
        if (userRequest.getGender() != null) {
            existingUser.setGender(userRequest.getGender());
        }

        User isUpdated = userRepo.save(existingUser);
        UserResponse userResponse = null;

        if (!ObjectUtils.isEmpty(isUpdated)){
            userResponse = getUserResponse(isUpdated);
        }
        return userResponse;
    }

    @Override
    public List<UserResponse> getAllUsers() {

        List<User> userList = userRepo.findAll();

        return userList.stream().map(UserServiceImpl::getUserResponse
                )
                .toList();
    }

    @Override
    public void deleteUser(Integer userId) {

        User user = userRepo.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found with id : " + userId)
        );

        userRepo.delete(user);

    }

    @Override
    public boolean followUser(Integer userId) {

        Integer loggedInUserId = util.getLoggedInUserDetails().getId();

        if (loggedInUserId.equals(userId)) {
            throw new IllegalArgumentException("You can't follow/unfollow yourself");
        }

        User follower = userRepo.findById(loggedInUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + loggedInUserId));
        User followee = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        boolean isFollowing = follower.getFollowing().contains(userId);

        if (isFollowing) {
            follower.getFollowing().remove(userId);
            followee.getFollowers().remove(loggedInUserId);
        } else {
            follower.getFollowing().add(userId);
            followee.getFollowers().add(loggedInUserId);
        }

        userRepo.save(follower);
        userRepo.save(followee);
        // Returns true if now following, false if now unfollowed
        return !isFollowing;
    }


    @Override
    public List<UserResponse> searchUser(String query) {

        List<User> searchedUser = userRepo.searchUser(query);

        return searchedUser.stream().map(UserServiceImpl::getUserResponse).toList();
    }


    public static UserResponse getUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .gender(user.getGender())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .following(user.getFollowing())
                .followers(user.getFollowers())
                .savedPost(user.getSavedPost().stream().map(post -> PostResponse.builder()
                        .postId(post.getId())
                        .image(post.getImage())
                        .video(post.getVideo())
                        .caption(post.getCaption())
                        .likedByUserIds(post.getLike().stream().map(User::getId).toList())
                        .createdAt(post.getCreatedAt())
                        .authorId(post.getUser().getId())
                        .authorEmail(post.getUser().getEmail())
                        .build()).toList())
                .build();
    }
}
