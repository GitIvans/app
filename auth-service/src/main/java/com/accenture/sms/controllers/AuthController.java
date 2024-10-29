package com.accenture.sms.controllers;

import com.accenture.sms.clients.UserServiceClient;
import com.accenture.sms.dto.LoginRequestDTO;
import com.accenture.sms.dto.UserInfoDTO;
import com.accenture.sms.security.jwt.JwtUtil;
import com.accenture.sms.service.AuthService;
import com.accenture.sms.service.CustomUserDetailsService;
import com.accenture.sms.swagger.DescriptionVariables;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserServiceClient userServiceClient;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final AuthService authService;

    @Autowired
    public AuthController(UserServiceClient userServiceClient, PasswordEncoder passwordEncoder, AuthService authService, CustomUserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userServiceClient = userServiceClient;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserInfoDTO userInfo) {
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));

        Boolean exists = userServiceClient.checkUsernameAndEmail(userInfo.getUsername(), userInfo.getEmail());
        if (exists) {
            return ResponseEntity.badRequest().body("Username or Email already exists");
        }

        userServiceClient.createUser(userInfo);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequest) {
        log.info("Attempting to log in user: {}", loginRequest.getUsername());
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());

        if (userDetails != null && passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
            String jwtToken = jwtUtil.generateToken(userDetails.getUsername());
            return ResponseEntity.ok("Bearer " + jwtToken);
        } else {
            log.warn("Invalid username or password for user: {}", loginRequest.getUsername());
            return ResponseEntity.badRequest().body("Invalid username or password");
        }
    }

//    @PostMapping("/register")
//    @ApiOperation(value = "Register a new user", tags = {DescriptionVariables.AUTH_CONTROLLER})
//    public ResponseEntity<String> register(@Valid @RequestBody UserInfoDTO userInfo) {
//        log.info("Registering user: {}", userInfo.getUsername());
//        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
//
//        Boolean exists = userServiceClient.checkUsernameAndEmail(userInfo.getUsername(), userInfo.getEmail());
//        if (exists) {
//            log.warn("Username or Email already exists for user: {}", userInfo.getUsername());
//            return ResponseEntity.badRequest().body("Username or Email already exists");
//        }
//
//        userServiceClient.createUser(userInfo);
//        log.info("User registered successfully: {}", userInfo.getUsername());
//        return ResponseEntity.ok("User registered successfully");
//    }
//
//    @PostMapping("/login")
//    @ApiOperation(value = "User login", tags = {DescriptionVariables.AUTH_CONTROLLER})
//    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
//        log.info("Attempting to log in user: {}", loginRequest.getUsername());
//        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
//
//        if (userDetails != null && passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
//            String jwtToken = jwtUtil.generateToken(userDetails.getUsername());
//            log.info("User logged in successfully: {}", loginRequest.getUsername());
//            return ResponseEntity.ok("Bearer " + jwtToken);
//        } else {
//            log.warn("Invalid username or password for user: {}", loginRequest.getUsername());
//            return ResponseEntity.badRequest().body("Invalid username or password");
//        }
//    }
//
//
//    private ResponseEntity<?> validateBindingResult(BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            log.error("Validation errors: {}", bindingResult);
//            List<String> errorMessages = bindingResult.getFieldErrors().stream()
//                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
//                    .collect(Collectors.toList());
//
//            return ResponseEntity.badRequest().body(errorMessages);
//        }
//        return null;
//    }
}

