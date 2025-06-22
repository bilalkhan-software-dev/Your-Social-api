package com.yoursocial.endpoint;


import com.yoursocial.dto.StoryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/story")
public interface StoryControllerEndpoint {


    @PostMapping("/create")
    public ResponseEntity<?> createStory(@RequestBody StoryRequest storyRequest);

    @GetMapping("/user")
    public ResponseEntity<?> userStory();

    @DeleteMapping("/delete/{storyId}")
    public ResponseEntity<?> deleteUserStory(@PathVariable Integer storyId);


}