package com.accenture.sms.clients;

import com.accenture.sms.dto.SubscriptionInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class SubscriptionServiceClient {

    private final WebClient webClient;

    @Autowired
    public SubscriptionServiceClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<SubscriptionInfoDTO> getSubscriptionsByUserId(Long userId) {
        return webClient.get()
                .uri("/api/subscription/user/{userId}", userId)
                .retrieve()
                .bodyToFlux(SubscriptionInfoDTO.class)
                .collectList()
                .block();
    }
}
