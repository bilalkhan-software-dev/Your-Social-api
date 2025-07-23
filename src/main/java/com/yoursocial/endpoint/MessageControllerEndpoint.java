package com.yoursocial.endpoint;


import com.yoursocial.dto.MessageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/message")
public interface MessageControllerEndpoint {

    @PostMapping("/create/chat/{chatId}")
    public ResponseEntity<?> createMessage(@PathVariable Integer chatId, @RequestBody MessageRequest messageRequest);

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<?> getAllMessageOfTheChat(@PathVariable Integer chatId);


}
