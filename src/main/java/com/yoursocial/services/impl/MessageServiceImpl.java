package com.yoursocial.services.impl;

import com.yoursocial.dto.ChatResponse;
import com.yoursocial.dto.MessageRequest;
import com.yoursocial.dto.MessageResponse;
import com.yoursocial.dto.MessageResponse.MessageUserDetails;
import com.yoursocial.entity.Chat;
import com.yoursocial.entity.Message;
import com.yoursocial.entity.User;
import com.yoursocial.exception.ResourceNotFoundException;
import com.yoursocial.repository.ChatRepository;
import com.yoursocial.repository.MessageRepository;
import com.yoursocial.services.MessageService;
import com.yoursocial.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final ModelMapper mapper;
    private final CommonUtil util;

    @Override
    public MessageResponse createdMessage(Integer chatId, MessageRequest messageRequest) {
        User loggedInUser = util.getLoggedInUserDetails();
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with id: " + chatId));

        // Verify user is part of the chat
        if (!chat.getUsers().contains(loggedInUser)) {
            throw new IllegalArgumentException("User is not part of this chat");
        }

        Message message = mapper.map(messageRequest, Message.class);
        message.setMessageCreatedAt(LocalDateTime.now());
        message.setUser(loggedInUser);
        message.setChat(chat);

        Message savedMessage = messageRepository.save(message);
        return getMessageResponse(savedMessage);
    }

    @Override
    public List<MessageResponse> findChatMessage(Integer chatId) {
        // Verify chat exists
        if (!chatRepository.existsById(chatId)) {
            throw new ResourceNotFoundException("Chat not found with id: " + chatId);
        }

        List<Message> messages = messageRepository.findByChatIdOrderByMessageCreatedAtAsc(chatId);
        return messages.stream()
                .map(this::getMessageResponse)
                .toList();
    }

    private MessageResponse getMessageResponse(Message message) {
        return MessageResponse.builder()
                .messageId(message.getId())
                .content(message.getContent())
                .image(message.getImage())
                .messageCreatedAt(message.getMessageCreatedAt())
                .messageUserDetails(MessageUserDetails.builder()
                        .userId(message.getUser().getId())
                        .fullName(message.getUser().getFirstName() + " " + message.getUser().getLastName())
                        .email(message.getUser().getEmail())
                        .build())
                .chatDetails(ChatResponse.builder()
                        .id(message.getChat().getId())
                        .chatName(message.getChat().getChatName())
                        .chatImage(message.getChat().getChatImage())
                        .chatCreatAt(message.getChat().getChatCreatedAt())
                        .build())
                .build();
    }
}
