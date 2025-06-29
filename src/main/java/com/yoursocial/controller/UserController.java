package com.yoursocial.controller;

import com.yoursocial.dto.UpdateUserRequest;
import com.yoursocial.dto.UserResponse;
import com.yoursocial.endpoint.UserControllerEndpoint;
import com.yoursocial.services.UserService;
import com.yoursocial.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class UserController implements UserControllerEndpoint {

    private final UserService userService;
    private final CommonUtil response;


    @Override
    public ResponseEntity<?> useProfile() {


        UserResponse userResponse = userService.showUserProfile();
        if (userResponse == null){
            return response.createErrorResponseMessage("No user found maybe token invalid!",HttpStatus.UNAUTHORIZED);
        }
        return response.createBuildResponse(userResponse,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteUser(Integer userId) {

        userService.deleteUser(userId);

        return response.createBuildResponseMessage("account deleted successfully!", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllUsers() {

        List<UserResponse> allUsers = userService.getAllUsers();

        if (CollectionUtils.isEmpty(allUsers)) {
            return response.createBuildResponseMessage("no user registered till yet", HttpStatus.NO_CONTENT);
        }

        return response.createBuildResponse(allUsers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> searchUser(String query) {

        List<UserResponse> searchedUser = userService.searchUser(query);


        if (CollectionUtils.isEmpty(searchedUser)) {
            return response.createBuildResponseMessage("no result found with query: " + query, HttpStatus.BAD_REQUEST);
        }
        return response.createBuildResponse(searchedUser, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateUser(UpdateUserRequest userRequest) {

        boolean b = userService.updateUser(userRequest);
        if (b) {
            return response.createBuildResponseMessage("user account updated successfully!", HttpStatus.OK);
        }

        return response.createErrorResponseMessage("update failed", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<?> followUser(Integer followedUserId) {

        boolean isFollowed = userService.followUser(followedUserId);

        UserResponse user = userService.findUserById(followedUserId);

        if (isFollowed) {
            return response.createBuildResponseMessage("successfully follow " + user.getFirstName() + " " + user.getLastName(), HttpStatus.OK);
        }

        return response.createBuildResponseMessage("successfully unfollow " + user.getFirstName() + " " + user.getLastName(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getUserDetailsById(Integer userId) {

        UserResponse user = userService.findUserById(userId);

        return response.createBuildResponse(user, HttpStatus.OK);
    }
}
