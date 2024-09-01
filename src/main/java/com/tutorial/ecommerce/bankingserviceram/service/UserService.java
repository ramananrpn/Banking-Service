package com.tutorial.ecommerce.bankingserviceram.service;


import com.tutorial.ecommerce.bankingserviceram.dto.UserRegistrationRequest;
import com.tutorial.ecommerce.bankingserviceram.exception.BadRequestException;
import com.tutorial.ecommerce.bankingserviceram.model.Role;
import com.tutorial.ecommerce.bankingserviceram.model.User;
import com.tutorial.ecommerce.bankingserviceram.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User saveUser(UserRegistrationRequest request) {
    validateRequest(request);

    User user = new User();
    user.setUsername(request.getUsername());
    user.setPassword(request.getPassword());
    user.setEmail(request.getEmail());
    user.setRole(Role.valueOf(request.getRole()));

    return userRepository.save(user);
  }

  private void validateRequest(UserRegistrationRequest request) {
    Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
    if (existingUser.isPresent()) {
      throw new BadRequestException("Email already exists");
    }

    try {
      Role.valueOf(request.getRole());
    } catch (IllegalArgumentException e) {
      throw new BadRequestException("Invalid role, must be one of: ADMIN, USER");
    }
  }

  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new BadCredentialsException("User not found"));
    UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
            .username(user.getEmail()) // Use email as username
            .password(user.getPassword())
            .roles(user.getRole().name())
            .build();
    return userDetails;
  }
}
