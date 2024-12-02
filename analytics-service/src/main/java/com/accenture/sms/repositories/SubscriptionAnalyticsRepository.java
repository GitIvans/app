package com.accenture.sms.repositories;

import com.accenture.sms.repositories.model.SubscriptionAnalyticsDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionAnalyticsRepository extends JpaRepository<SubscriptionAnalyticsDAO, Long> {

    List<SubscriptionAnalyticsDAO> findByUserId(Long userId);
    SubscriptionAnalyticsDAO findFirstByUserId(Long userId);
}
