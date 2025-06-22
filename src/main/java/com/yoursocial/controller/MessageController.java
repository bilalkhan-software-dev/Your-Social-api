package com.yoursocial.controller;

import com.yoursocial.dto.MessageRequest;
import com.yoursocial.dto.MessageResponse;
import com.yoursocial.endpoint.MessageControllerEndpoint;
import com.yoursocial.services.MessageService;
import com.yoursocial.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessageController implements MessageControllerEndpoint {

    private final MessageService messageService;
    private final CommonUtil response;

    @Override
    public ResponseEntity<?> createMessage(Integer chatId, MessageRequest messageRequest) {

        boolean isMessageCreated = messageService.createdMessage(chatId, messageRequest);

        if (isMessageCreated) {
            return response.createBuildResponseMessage("Message successfully created!", HttpStatus.CREATED);
        }

        return response.createErrorResponseMessage("Message creation failed!", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    public ResponseEntity<?> getAllMessageOfTheChat(Integer chatId) {


        List<MessageResponse> chatMessages = messageService.findChatMessage(chatId);

        if (CollectionUtils.isEmpty(chatMessages)) {
            return ResponseEntity.noContent().build();
        }

        return response.createBuildResponse(chatMessages, HttpStatus.OK);
    }
}
