package com.yoursocial.services.impl;

import com.yoursocial.dto.UpdateUserRequest;
import com.yoursocial.dto.UserResponse;
import com.yoursocial.entity.User;
import com.yoursocial.exception.ResourceNotFoundException;
import com.yoursocial.repository.UserRepository;
import com.yoursocial.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final ModelMapper mapper;

    @Override
    public UserResponse findUserById(Integer userId) {

        User isUserFound = userRepo.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not available or created with id : " + userId)
        );
        UserResponse userResponse = mapper.map(isUserFound, UserResponse.class);

        return !ObjectUtils.isEmpty(userResponse) ? userResponse : null;
    }

    @Override
    public UserResponse findUserByEmail(String email) {

        User isUserFound = userRepo.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User not available or created with email : " + email)
        );

        UserResponse userResponse = mapper.map(isUserFound, UserResponse.class);

        return !ObjectUtils.isEmpty(userResponse) ? userResponse : null;
    }

    @Override
    public boolean updateUser(UpdateUserRequest userRequest) {

        Integer userId = 1;
        User existingUser = userRepo.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found with id:" + userId)
        );

        mapper.map(userRequest, existingUser);

        User isUpdated = userRepo.save(existingUser);

        return !ObjectUtils.isEmpty(isUpdated);
    }

    @Override
    public List<UserResponse> getAllUsers() {

        List<User> userList = userRepo.findAll();

        return userList.stream().map(user -> mapper.map(user, UserResponse.class)).toList();

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

        Integer loggedInUserId = 1;  // login user wants to follow other users and userId is which is the user wants to follow

        User requestedUser = userRepo.findById(loggedInUserId).orElseThrow(
                () -> new ResourceNotFoundException("user not found with id :" + loggedInUserId)
        );
        User user = userRepo.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("user not found with id :" + userId)
        );

        boolean addFollower = requestedUser.getFollowing().add(user.getId());// increase following
        boolean addFollowing = user.getFollowers().add(requestedUser.getId());// increase followers

        return addFollower && addFollowing ? true : false;
    }

    @Override
    public List<UserResponse> searchUser(String query) {

        List<User> searchedUser = userRepo.searchUser(query);

        return searchedUser.stream().map(user -> mapper.map(user, UserResponse.class)).toList();
    }
}
