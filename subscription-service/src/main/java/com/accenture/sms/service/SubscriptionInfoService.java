package com.accenture.sms.service;

import com.accenture.sms.model.SubscriptionInfo;
import java.util.List;

public interface SubscriptionInfoService {

    SubscriptionInfo saveSubscriptionInfo(SubscriptionInfo subscriptionInfo);

    SubscriptionInfo updateSubscriptionInfo(Long subscriptionId, SubscriptionInfo subscriptionInfoUpdate);

    SubscriptionInfo getSubscriptionInfoById(Long subscriptionId);

    List<SubscriptionInfo> getAllSubscriptions();

    void deleteSubscriptionInfo(Long subscriptionId);

    List<SubscriptionInfo> getSubscriptionsByUserId(Long userId);
}
