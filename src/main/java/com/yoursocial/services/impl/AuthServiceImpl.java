package com.yoursocial.services.impl;

import com.yoursocial.dto.UserRequest;
import com.yoursocial.entity.User;
import com.yoursocial.repository.UserRepository;
import com.yoursocial.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Override
    public boolean registerUser(UserRequest user) {

        User mappedUser = mapper.map(user, User.class);

        User saved = userRepository.save(mappedUser);

        return !ObjectUtils.isEmpty(saved);
    }


}
