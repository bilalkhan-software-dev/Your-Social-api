package com.yoursocial.controller;

import com.yoursocial.dto.UserRequest;
import com.yoursocial.endpoint.AuthControllerEndpoint;
import com.yoursocial.services.AuthService;
import com.yoursocial.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthControllerEndpoint {

    private final AuthService authService;
    private final CommonUtil response;

    @Override
    public ResponseEntity<?> registerUser(UserRequest userRequest) {

        boolean isRegistered = authService.registerUser(userRequest);

        if (isRegistered) {
            return response.createBuildResponseMessage("user registered successfully", HttpStatus.CREATED);
        }

        return response.createErrorResponseMessage("user not registered!", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
