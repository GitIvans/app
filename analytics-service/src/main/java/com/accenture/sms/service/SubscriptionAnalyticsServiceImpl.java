package com.accenture.sms.service;

import com.accenture.sms.dto.SubscriptionAnalysisResultDTO;
import com.accenture.sms.dto.SubscriptionInfoDTO;
import com.accenture.sms.repositories.SubscriptionAnalyticsRepository;
import com.accenture.sms.repositories.model.SubscriptionAnalyticsDAO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Service
public class SubscriptionAnalyticsServiceImpl {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private SubscriptionAnalyticsRepository analyticsRepository;

    public SubscriptionAnalysisResultDTO analyzeUserSubscriptions(Long userId) {
        List<SubscriptionInfoDTO> subscriptions = webClientBuilder.build()
                .get()
                .uri("http://localhost:8082/api/subscription/user/{userId}", userId)
                .retrieve()
                .bodyToFlux(SubscriptionInfoDTO.class)
                .collectList()
                .block();
        if (subscriptions == null || subscriptions.isEmpty()) {
            return new SubscriptionAnalysisResultDTO(0, 0, 0.0, 0.0, Map.of());
        }

        double totalCost = subscriptions.stream().mapToDouble(SubscriptionInfoDTO::getCost).sum();
        int totalSubscriptions = subscriptions.size();
        int activeSubscriptions = (int) subscriptions.stream()
                .filter(sub -> sub.getPaymentDate().isAfter(LocalDate.now()) || sub.getPaymentDate().isEqual(LocalDate.now()))
                .count();
        double averageCost = totalSubscriptions > 0 ? totalCost / totalSubscriptions : 0.0;

        Map<String, Long> subscriptionsByCategory = subscriptions.stream()
                .collect(Collectors.groupingBy(SubscriptionInfoDTO::getSubscriptionType, Collectors.counting()));

        SubscriptionAnalyticsDAO existingAnalytics = analyticsRepository.findFirstByUserId(userId);

        if (existingAnalytics != null) {
            existingAnalytics.setTotalCost(totalCost);
            existingAnalytics.setTotalSubscriptions(totalSubscriptions);
            existingAnalytics.setActiveSubscriptions(activeSubscriptions);
            existingAnalytics.setAverageCost(averageCost);
            analyticsRepository.save(existingAnalytics);
        } else {
            SubscriptionAnalyticsDAO newAnalytics = new SubscriptionAnalyticsDAO(
                    null, userId, totalCost, totalSubscriptions, activeSubscriptions, averageCost
            );
            analyticsRepository.save(newAnalytics);
        }

        return new SubscriptionAnalysisResultDTO(totalSubscriptions, activeSubscriptions, totalCost, averageCost, subscriptionsByCategory);
    }
}

