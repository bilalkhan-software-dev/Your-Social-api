package com.yoursocial.controller;

import com.yoursocial.dto.LoginRequest;
import com.yoursocial.dto.LoginResponse;
import com.yoursocial.dto.UserRequest;
import com.yoursocial.endpoint.AuthControllerEndpoint;
import com.yoursocial.services.AuthService;
import com.yoursocial.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController implements AuthControllerEndpoint {

    private final AuthService authService;
    private final CommonUtil response;

    @Override
    public ResponseEntity<?> checkUsernameTakenOrNot(String email) {

        boolean isAlreadyTaken = authService.checkUsernameAlreadyTakenOrNot(email);
        if (isAlreadyTaken) {
            return response.createBuildResponseMessage("Email already taken", HttpStatus.OK);
        }

        return response.createBuildResponseMessage("Email is available", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> registerUser(UserRequest userRequest) {

        boolean isRegistered = authService.registerUser(userRequest);

        if (isRegistered) {
            return response.createBuildResponseMessage("Account created successfully", HttpStatus.CREATED);
        }

        return response.createErrorResponseMessage("Account not created! Something wrong on server", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<?> login(LoginRequest user) {

        log.info("user login email: {}  password: {}", user.getEmail(), user.getPassword());

        LoginResponse loginResponse = authService.login(user);

        if (!ObjectUtils.isEmpty(loginResponse)) {
            return response.createBuildResponse("User login Successfully!",loginResponse, HttpStatus.OK);
        }

        return response.createErrorResponseMessage("Invalid credentials", HttpStatus.BAD_REQUEST);
    }


}
