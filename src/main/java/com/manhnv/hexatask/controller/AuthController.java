package com.manhnv.hexatask.controller;

import com.manhnv.hexatask.dto.request.ResetPasswordRequest;
import com.manhnv.hexatask.dto.request.RoleRequest;
import com.manhnv.hexatask.dto.request.SignInRequest;
import com.manhnv.hexatask.dto.request.SignUpRequest;
import com.manhnv.hexatask.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/forgot-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        authService.sendMailResetPassword(request.getEmail());
        return ResponseEntity.ok("Password reset link sent to your email.");
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authService.signUp(request));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInRequest request) {
        return ResponseEntity.ok(authService.signIn(request));
    }

    @PostMapping("/role")
    public ResponseEntity<String> createRole(@Valid @RequestBody RoleRequest request) {
        return ResponseEntity.ok(authService.createRole(request));
    }
}
