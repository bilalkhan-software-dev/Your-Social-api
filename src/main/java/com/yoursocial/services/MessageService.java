package com.yoursocial.services;

import com.yoursocial.dto.MessageRequest;
import com.yoursocial.dto.MessageResponse;

import java.util.List;

public interface MessageService {

    MessageResponse createdMessage(Integer chatId,MessageRequest messageRequest);

    List<MessageResponse> findChatMessage(Integer chatId);



}
