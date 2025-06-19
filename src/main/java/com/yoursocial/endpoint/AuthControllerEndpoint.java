package com.yoursocial.endpoint;

import com.yoursocial.dto.LoginRequest;
import com.yoursocial.dto.UserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/auth") // not required authentication
public interface AuthControllerEndpoint {


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequest userRequest);

    @GetMapping("/login")
    public ResponseEntity<?> login (@RequestBody LoginRequest user);


}
