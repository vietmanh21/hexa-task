package com.manhnv.hexatask.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    @Value("${application.email.token-expiration}")
    private long tokenExpiration;

    private final EmailService emailService;
    private final RedisService redisService;

    public void sendMailResetPassword(String email) {
        String token = UUID.randomUUID().toString().substring(0, 8);
        if (Objects.nonNull(redisService.get(email))) {
            redisService.del(email);
        }
        redisService.set(email, token, tokenExpiration);
        emailService.sendEmail(email, token);
    }
}
