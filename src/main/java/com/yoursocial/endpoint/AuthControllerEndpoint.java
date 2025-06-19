package com.yoursocial.endpoint;

import com.yoursocial.dto.UserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/auth") // not required authentication
public interface AuthControllerEndpoint {

    public ResponseEntity<?> registerUser(@RequestBody UserRequest userRequest);
}
