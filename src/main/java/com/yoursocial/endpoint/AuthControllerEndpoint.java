package com.yoursocial.endpoint;

import com.yoursocial.dto.LoginRequest;
import com.yoursocial.dto.UserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/auth") // not required authentication
public interface AuthControllerEndpoint {

    @PostMapping("/check/{email}")
    public ResponseEntity<?> checkUsernameTakenOrNot(@PathVariable String email);

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequest userRequest);

    @GetMapping("/login")
    public ResponseEntity<?> login (@RequestBody LoginRequest user);


}
