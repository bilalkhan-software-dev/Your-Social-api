package com.yoursocial.endpoint;

import com.yoursocial.dto.CommentRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/v1/comment")
public interface CommentControllerEndpoint {


    @PostMapping("/create/post/{postId}")
    public ResponseEntity<?> createPost(@RequestBody CommentRequest commentRequest,@PathVariable Integer postId);

    @GetMapping("/all/{postId}")
    public ResponseEntity<?> getAllCommentOfThePost(@PathVariable Integer postId);

    @GetMapping("/all")
    public ResponseEntity<?> getAllComment();

    @GetMapping("/{commentId}")
    public ResponseEntity<?> detailsOfTheComment(@PathVariable Integer commentId);

    @PutMapping("/like/{commentId}")
    public ResponseEntity<?> likeComment(@PathVariable Integer commentId);







}
