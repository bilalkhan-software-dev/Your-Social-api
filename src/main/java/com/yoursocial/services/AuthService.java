package com.yoursocial.services;

import com.yoursocial.dto.UserRequest;

public interface AuthService {

    boolean registerUser(UserRequest userRequest);
}
