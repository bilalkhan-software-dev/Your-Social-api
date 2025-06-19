package com.yoursocial.services;


import com.yoursocial.dto.UpdateUserRequest;
import com.yoursocial.dto.UserRequest;
import com.yoursocial.dto.UserResponse;

import javax.swing.*;
import java.util.List;

public interface UserService {


    UserResponse findUserById(Integer userId);

    UserResponse findUserByEmail(String email);

    boolean updateUser(UpdateUserRequest userRequest);

    List<UserResponse> searchUser(String query);

    List<UserResponse> getAllUsers();

    boolean followUser(Integer userId);

    void deleteUser(Integer userId);





}
