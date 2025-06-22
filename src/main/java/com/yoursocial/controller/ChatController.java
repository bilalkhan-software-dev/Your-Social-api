package com.yoursocial.controller;

import com.yoursocial.dto.ChatRequest;
import com.yoursocial.dto.ChatResponse;
import com.yoursocial.endpoint.ChatControllerEndpoint;
import com.yoursocial.services.ChatService;
import com.yoursocial.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController implements ChatControllerEndpoint {

    private final ChatService chatService;
    private final CommonUtil response;

    @Override
    public ResponseEntity<?> createChat(ChatRequest chatRequest) {

        boolean isChatCreated = chatService.createChat(chatRequest);
        if (isChatCreated) {
            return response.createBuildResponseMessage("Chat created successfully!", HttpStatus.CREATED);
        }

        return response.createBuildResponseMessage("Chat already created!", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> allChatsOfTheLoggedInCreatedUser() {

        List<ChatResponse> allCreatedChatsOfTheUser = chatService.getAllCreatedChatsOfTheUser();

        if (CollectionUtils.isEmpty(allCreatedChatsOfTheUser)) {
            return ResponseEntity.noContent().build();
        }

        return response.createBuildResponse(allCreatedChatsOfTheUser, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> chatDetailsByID(Integer chatId) {

        ChatResponse chatDetail = chatService.findChatById(chatId);

        if (chatDetail != null) {
            return response.createBuildResponse(chatDetail, HttpStatus.OK);
        }

        return response.createErrorResponseMessage("Something went wrong on server. Chat is empty", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<?> deleteChatOfTheUser(Integer chatId) {

        chatService.deleteChatOfTheAuthenticateUser(chatId);

        return response.createBuildResponseMessage("Chat deleted successfully!", HttpStatus.OK);
    }
}
