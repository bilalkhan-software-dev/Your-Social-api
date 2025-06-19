package com.yoursocial.services;

import com.yoursocial.dto.LoginRequest;
import com.yoursocial.dto.LoginResponse;
import com.yoursocial.dto.UserRequest;

public interface AuthService {

    boolean registerUser(UserRequest userRequest);

    LoginResponse login(LoginRequest user);
}
