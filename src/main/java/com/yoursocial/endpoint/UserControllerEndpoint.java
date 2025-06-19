package com.yoursocial.endpoint;

import com.yoursocial.dto.UpdateUserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/user") // required authentication
public interface UserControllerEndpoint {

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer userId);


    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers();

    @GetMapping("/search/{query}")
    public ResponseEntity<?> searchUser(@PathVariable String query);

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest userRequest);

    @PutMapping("/follow/{userId}")
    public ResponseEntity<?> followUser(@PathVariable("userId") Integer followedUserId);

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserDetailsById(@PathVariable Integer userId);



}
