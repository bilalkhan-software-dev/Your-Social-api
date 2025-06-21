package com.yoursocial.services.impl;

import com.yoursocial.dto.PostResponse;
import com.yoursocial.dto.UpdateUserRequest;
import com.yoursocial.dto.UserResponse;
import com.yoursocial.entity.User;
import com.yoursocial.exception.ResourceNotFoundException;
import com.yoursocial.repository.UserRepository;
import com.yoursocial.services.UserService;
import com.yoursocial.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final CommonUtil util;

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
    public boolean updateUser(UpdateUserRequest userRequest) {

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

        return !ObjectUtils.isEmpty(isUpdated);
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

        Integer loggedInUserId = util.getLoggedInUserDetails().getId();  // login user wants to follow other users and userId is which is the user wants to follow

        User requestedUser = userRepo.findById(loggedInUserId).orElseThrow(
                () -> new ResourceNotFoundException("user not found with id:" + loggedInUserId)
        );
        User user = userRepo.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("user not found with id:" + userId)
        );

        if (loggedInUserId.equals(userId)) {
            throw new IllegalArgumentException("you can't follow yourself");
        }

        boolean addFollower = requestedUser.getFollowing().add(user.getId());// increase following
        boolean addFollowing = user.getFollowers().add(requestedUser.getId());// increase followers


        userRepo.save(requestedUser);
        userRepo.save(user);
        return addFollower && addFollowing ? true : false;
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
