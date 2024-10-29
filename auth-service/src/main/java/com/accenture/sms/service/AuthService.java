package com.accenture.sms.service;

import com.accenture.sms.dto.UserInfoDTO;
import com.accenture.sms.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String generateToken(UserInfoDTO userInfo) {
        return jwtTokenProvider.generateToken(userInfo.getUsername());
    }
}
