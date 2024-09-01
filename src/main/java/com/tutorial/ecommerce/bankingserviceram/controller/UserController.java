package com.tutorial.ecommerce.bankingserviceram.controller;

import com.tutorial.ecommerce.bankingserviceram.dto.UserRegistrationRequest;
import com.tutorial.ecommerce.bankingserviceram.model.User;
import com.tutorial.ecommerce.bankingserviceram.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserRegistrationRequest user) {
        User savedUser = userService.saveUser(user);
        log.info("User {} registered successfully", savedUser.getId());
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }
}
