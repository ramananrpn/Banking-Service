package com.tutorial.ecommerce.bankingserviceram.controller;

import com.tutorial.ecommerce.bankingserviceram.dto.LoginRequest;
import com.tutorial.ecommerce.bankingserviceram.dto.response.JwtResponse;
import com.tutorial.ecommerce.bankingserviceram.exception.BadRequestException;
import com.tutorial.ecommerce.bankingserviceram.model.User;
import com.tutorial.ecommerce.bankingserviceram.security.JwtTokenProvider;
import com.tutorial.ecommerce.bankingserviceram.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtTokenProvider jwtUtil;

    private final UserService userService;

    public AuthController(JwtTokenProvider jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        User user = userService.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new BadRequestException("User not found"));

        if (user.getPassword() == null || !user.getPassword().equals(loginRequest.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String jwtToken = jwtUtil.generateToken(user.getEmail(), String.valueOf(user.getRole()));
        return ResponseEntity.ok(new JwtResponse(jwtToken));
    }
}