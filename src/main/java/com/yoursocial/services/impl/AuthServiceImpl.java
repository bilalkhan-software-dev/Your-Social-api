package com.yoursocial.services.impl;

import com.yoursocial.config.security.CustomUserDetails;
import com.yoursocial.dto.LoginRequest;
import com.yoursocial.dto.LoginResponse;
import com.yoursocial.dto.UserRequest;
import com.yoursocial.dto.UserResponse;
import com.yoursocial.entity.User;
import com.yoursocial.exception.ExistDataException;
import com.yoursocial.repository.UserRepository;
import com.yoursocial.services.AuthService;
import com.yoursocial.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public boolean registerUser(UserRequest user) {


        boolean isExist = userRepository.existsByEmail(user.getEmail());
        if (isExist){
            throw new ExistDataException("account already registered with username:"+user.getEmail() + ". Please try with another email!");
        }

        User mappedUser = mapper.map(user, User.class);

        mappedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userRepository.save(mappedUser);

        return !ObjectUtils.isEmpty(saved);
    }



    @Override
    public LoginResponse login(LoginRequest user) {

        String username = user.getEmail();
        String password = user.getPassword();
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        if (authenticate.isAuthenticated()){
            CustomUserDetails userDetails = (CustomUserDetails) authenticate.getPrincipal();

            String token = jwtService.generateToken(userDetails.getUser());

            return LoginResponse.builder()
                    .userDetails(mapper.map(userDetails.getUser(), UserResponse.class))
                    .token(token)
                    .build();
        }

        return null;
    }
}
