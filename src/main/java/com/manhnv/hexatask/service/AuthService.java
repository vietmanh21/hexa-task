package com.manhnv.hexatask.service;

import com.manhnv.hexatask.dto.request.RoleRequest;
import com.manhnv.hexatask.dto.request.SignInRequest;
import com.manhnv.hexatask.dto.request.SignUpRequest;
import com.manhnv.hexatask.dto.response.SignUpResponse;
import com.manhnv.hexatask.dto.response.UserResponse;
import com.manhnv.hexatask.exception.DuplicatedException;
import com.manhnv.hexatask.exception.NotfoundException;
import com.manhnv.hexatask.model.Role;
import com.manhnv.hexatask.model.User;
import com.manhnv.hexatask.repository.RoleRepository;
import com.manhnv.hexatask.repository.UserRepository;
import com.manhnv.hexatask.security.jwt.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    @Value("${application.email.token-expiration}")
    private long tokenResetPasswordExpiration;

    private final EmailService emailService;
    private final RedisService redisService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private static final String ROLE_USER = "USER";
    private static final String KEY_RESET_PASSWORD_PREFIX = "key:reset-password:";

    public void sendMailResetPassword(String email) {
        String token = UUID.randomUUID().toString().substring(0, 8);
        if (Objects.nonNull(redisService.get(KEY_RESET_PASSWORD_PREFIX + email))) {
            redisService.del(KEY_RESET_PASSWORD_PREFIX + email);
        }
        redisService.set(KEY_RESET_PASSWORD_PREFIX + email, token, tokenResetPasswordExpiration);
        emailService.sendEmail(email, token);
    }

    public SignUpResponse signUp(SignUpRequest request) {
        boolean emailExists = userRepository.existsByEmail(request.getEmail());
        if (emailExists) {
            throw new DuplicatedException("Email already exists");
        }
        boolean usernameExists = userRepository.existsByUserName(request.getUserName());
        if (usernameExists) {
            throw new DuplicatedException("Username already exists");
        }
        Role userRole = roleRepository.findByName(ROLE_USER).orElseThrow(() -> new NotfoundException("Role is not found"));
        User user = User.builder()
                .email(request.getEmail())
                .userName(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .roleIds(Collections.singleton(userRole.getId()))
                .build();
        userRepository.save(user);
        return new SignUpResponse("success", "Sign up successfully");
    }

    public UserResponse signIn(SignInRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotfoundException("User not found"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new NotfoundException("Invalid password");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AuthenticationException ex) {
            log.warn("Authentication exception: {}", ex.getMessage());
            throw new AuthenticationException("Authentication failed: " + ex.getMessage()) {
            };
        }

        String jwtToken = jwtService.generateToken(user.getUserName());
        String refreshToken = UUID.randomUUID().toString();
        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(user, userResponse);
        userResponse.setAccessToken(jwtToken);
        userResponse.setRefreshToken(refreshToken);

        return userResponse;
    }

    public String createRole(RoleRequest request) {
        boolean roleExists = roleRepository.existsByName(request.getName());
        if (roleExists) {
            throw new DuplicatedException("Role already exists");
        }
        Role role = new Role();
        role.setName(request.getName());
        roleRepository.save(role);
        return "success";
    }
}
