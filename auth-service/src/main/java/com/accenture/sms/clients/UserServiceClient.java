package com.accenture.sms.clients;

import com.accenture.sms.dto.UserInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class UserServiceClient {

    private final WebClient webClient;

    @Autowired
    public UserServiceClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Boolean checkUsernameAndEmail(String username, String email) {
        return webClient.get()
                .uri("/api/user/info/check?username={username}&email={email}", username, email)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }

    public void createUser(UserInfoDTO userInfo) {
        webClient.post()
                .uri("api/user/info")
                .bodyValue(userInfo)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public UserInfoDTO getUserByUsername(String username) {
        return webClient.get()
                .uri("/api/user/info/username/{username}", username)
                .retrieve()
                .bodyToMono(UserInfoDTO.class)
                .block();
    }
}
