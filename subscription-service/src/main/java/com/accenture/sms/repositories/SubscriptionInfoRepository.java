package com.accenture.sms.repositories;

import com.accenture.sms.repositories.model.SubscriptionInfoDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionInfoRepository extends JpaRepository<SubscriptionInfoDAO, Long> {
    List<SubscriptionInfoDAO> findByUserId(Long userId);

}
