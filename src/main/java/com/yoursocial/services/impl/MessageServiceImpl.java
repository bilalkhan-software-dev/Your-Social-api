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
    public boolean createdMessage(Integer chatId, MessageRequest messageRequest) {

        User loggedInUser = util.getLoggedInUserDetails();

        Message message = mapper.map(messageRequest, Message.class);

        Chat chat = chatRepository.findById(chatId).
                orElseThrow(
                        () -> new ResourceNotFoundException("Chat not found with id: " + chatId)
                );


        message.setMessageCreatedAt(LocalDateTime.now());
        message.setUser(loggedInUser);
        message.setChat(chat);

        Message isMessageCreated = messageRepository.save(message);

        return !ObjectUtils.isEmpty(isMessageCreated);
    }

    @Override
    public List<MessageResponse> findChatMessage(Integer chatId) {

        chatRepository.findById(chatId).orElseThrow(
                () -> new ResourceNotFoundException("Chat not found with id: "+ chatId)
        );

        List<Message> chats = messageRepository.findByChatId(chatId);

        return chats.stream().map(this::getMessageResponse)
                .toList();
    }


    private MessageResponse getMessageResponse(Message message) {
        return MessageResponse.builder()
                .messageId(message.getId())
                .image(message.getImage())
                .content(message.getContent())
                .messageCreatedAt(message.getMessageCreatedAt())
                .messageUserDetails(MessageUserDetails.builder()
                        .email(message.getUser().getEmail())
                        .userId(message.getUser().getId())
                        .fullName(message.getUser().getFirstName() + " " + message.getUser().getLastName())
                        .build())
                .chatDetails(ChatResponse.builder()
                        .id(message.getChat().getId())
                        .chatName(message.getChat().getChatName())
                        .chatImage(message.getChat().getChatImage())
                        .chatCreatAt(message.getChat().getChatCreatedAt())
                        .users(message.getChat().getUsers().stream().map(user -> ChatResponse.UserResponse.builder()
                                .userId(user.getId())
                                .fullName(user.getFirstName() + " " + user.getLastName())
                                .email(user.getEmail())
                                .build()).toList())
                        .build())
                .build();
    }

}
