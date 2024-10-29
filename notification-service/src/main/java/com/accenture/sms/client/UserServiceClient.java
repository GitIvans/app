package com.accenture.sms.client;

import com.accenture.sms.dto.UserInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class UserServiceClient {

    private final WebClient webClient;

    @Autowired
    public UserServiceClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public UserInfoDTO getUserById(Long userId) {
        return webClient.get()
                .uri("/api/user/info/{userId}", userId)
                .retrieve()
                .bodyToMono(UserInfoDTO.class)
                .block();
    }
}
