package com.accenture.sms.service;

import com.accenture.sms.clients.UserServiceClient;
import com.accenture.sms.dto.UserInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserServiceClient userServiceClient;

    @Autowired
    public CustomUserDetailsService(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserInfoDTO userInfo = userServiceClient.getUserByUsername(username);

        if (userInfo != null) {
            return User.withUsername(userInfo.getUsername())
                    .password(userInfo.getPassword())
                    .authorities(Collections.emptyList())
                    .accountExpired(false)
                    .accountLocked(false)
                    .credentialsExpired(false)
                    .disabled(false)
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
