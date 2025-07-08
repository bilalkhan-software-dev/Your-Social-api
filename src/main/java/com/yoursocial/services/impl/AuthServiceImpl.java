package com.yoursocial.services.impl;

import com.yoursocial.config.security.CustomUserDetails;
import com.yoursocial.dto.*;
import com.yoursocial.entity.User;
import com.yoursocial.exception.ExistDataException;
import com.yoursocial.repository.UserRepository;
import com.yoursocial.services.AuthService;
import com.yoursocial.services.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    @Override
    public boolean checkUsernameAlreadyTakenOrNot(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean registerUser(UserRequest user) {

        boolean isExist = checkUsernameAlreadyTakenOrNot(user.getEmail());
        if (isExist) {
            log.info("User already exist with username :{}", user.getEmail());
            throw new ExistDataException("Account already registered with username: " + user.getEmail() + " .Please try with another email!");
        }

        User mappedUser = mapper.map(user, User.class);

        mappedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        mappedUser.setBio("Hey there! I'm new here. More about me coming soon.");
        mappedUser.setImage("https://res.cloudinary.com/dkkgqafqw/image/upload/v1748716426/c20bf2b8-6e3a-4a18-8e69-63ebfe519f0d.png");
        mappedUser.setBanner("https://res.cloudinary.com/dkkgqafqw/image/upload/v1748633362/0150a160-666b-40a7-894f-443f83ef0ac5.jpg");
        User saved = userRepository.save(mappedUser);
        log.info("User registered  with username :{} and details :{}", user.getEmail(), user);
        return !ObjectUtils.isEmpty(saved);
    }
    @Override
    public LoginResponse login(LoginRequest user) {

        String username = user.getEmail();
        String password = user.getPassword();
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        if (authenticate.isAuthenticated()) {
            log.info("User is authenticate with email: {} and password: {} and now generating token", username, passwordEncoder.encode(password));
            CustomUserDetails userDetails = (CustomUserDetails) authenticate.getPrincipal();

            String token = jwtService.generateToken(userDetails.getUser());
            User authUserDetails = userDetails.getUser();
            return LoginResponse.builder()
                    .fullName(authUserDetails.getFirstName() + " " + authUserDetails.getLastName())
                    .email(authUserDetails.getEmail())
                    .token(token)
                    .build();
        }
        return null;
    }
}
