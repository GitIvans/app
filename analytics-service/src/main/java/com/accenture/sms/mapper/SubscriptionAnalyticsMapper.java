package com.accenture.sms.mapper;

import com.accenture.sms.model.SubscriptionAnalytics;
import com.accenture.sms.repositories.model.SubscriptionAnalyticsDAO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubscriptionAnalyticsMapper {

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "totalCost", target = "totalCost")
    SubscriptionAnalytics subscriptionAnalyticsDAOToSubscriptionAnalytics(SubscriptionAnalyticsDAO subscriptionAnalyticsDAO);

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "totalCost", target = "totalCost")
    SubscriptionAnalyticsDAO subscriptionAnalyticsToSubscriptionAnalyticsDAO(SubscriptionAnalytics subscriptionAnalytics);
}
