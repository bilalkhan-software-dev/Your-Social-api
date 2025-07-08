package com.yoursocial.endpoint;


import com.yoursocial.dto.PostRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/post")
public interface PostControllerEndpoint {

    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestBody PostRequest postRequest);

    @GetMapping("/all")
    public ResponseEntity<?> allPost();

    @GetMapping("/user/all")
    public ResponseEntity<?> allUserPost();

    @GetMapping("/{postId}")
    public ResponseEntity<?> findPostById(@PathVariable Integer postId);

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Integer postId
    );

    @PutMapping("/like/{postId}")
    public ResponseEntity<?> likePost(@PathVariable Integer postId);

    @PutMapping("/saved/{postId}")
    public ResponseEntity<?> savePost(@PathVariable Integer postId);


}
