package com.yoursocial.controller;

import com.yoursocial.dto.LoginRequest;
import com.yoursocial.dto.LoginResponse;
import com.yoursocial.dto.UserRequest;
import com.yoursocial.endpoint.AuthControllerEndpoint;
import com.yoursocial.services.AuthService;
import com.yoursocial.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthControllerEndpoint {

    private final AuthService authService;
    private final CommonUtil response;

    @Override
    public ResponseEntity<?> checkUsernameTakenOrNot(String email) {

        boolean isAlreadyTaken = authService.checkUsernameAlreadyTakenOrNot(email);
        if (isAlreadyTaken) {
            return response.createBuildResponseMessage("username already taken", HttpStatus.OK);
        }

        return response.createBuildResponseMessage("username is available", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> registerUser(UserRequest userRequest) {

        boolean isRegistered = authService.registerUser(userRequest);

        if (isRegistered) {
            return response.createBuildResponseMessage("account created successfully", HttpStatus.CREATED);
        }

        return response.createErrorResponseMessage("account not created!", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<?> login(LoginRequest user) {

        LoginResponse loginResponse = authService.login(user);

        if (!ObjectUtils.isEmpty(loginResponse)) {
            return response.createBuildResponse(loginResponse, HttpStatus.OK);
        }

        return response.createErrorResponseMessage("invalid credentials", HttpStatus.BAD_REQUEST);
    }

}
