package com.manhnv.hexatask.service;

import com.manhnv.hexatask.dto.request.SignUpRequest;
import com.manhnv.hexatask.dto.response.UserResponse;
import com.manhnv.hexatask.exception.DuplicatedException;
import com.manhnv.hexatask.exception.NotfoundException;
import com.manhnv.hexatask.model.User;
import com.manhnv.hexatask.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    @Value("${application.email.token-expiration}")
    private long tokenExpiration;

    private final EmailService emailService;
    private final RedisService redisService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void sendMailResetPassword(String email) {
        String token = UUID.randomUUID().toString().substring(0, 8);
        if (Objects.nonNull(redisService.get(email))) {
            redisService.del(email);
        }
        redisService.set(email, token, tokenExpiration);
        emailService.sendEmail(email, token);
    }

    public UserResponse signUp(SignUpRequest request) {
        UserResponse userResponse = new UserResponse();
        boolean emailExists = userRepository.existsByEmail(request.getEmail());
        if (emailExists) {
            throw new DuplicatedException("Email already exists");
        }
        boolean usernameExists = userRepository.existsByUsername(request.getUserName());
        if (usernameExists) {
            throw new DuplicatedException("Username already exists");
        }
        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);
        BeanUtils.copyProperties(user, userResponse);
        return userResponse;
    }

}
