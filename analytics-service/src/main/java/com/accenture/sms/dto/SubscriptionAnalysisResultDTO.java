package com.accenture.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionAnalysisResultDTO {

    private int totalSubscriptions;
    private int activeSubscriptions;
    private double totalCost;
    private double averageCost;
    private Map<String, Long> subscriptionsByCategory;

}
