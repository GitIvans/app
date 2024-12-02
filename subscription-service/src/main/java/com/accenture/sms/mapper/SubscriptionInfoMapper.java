package com.accenture.sms.mapper;

import com.accenture.sms.model.SubscriptionInfo;
import com.accenture.sms.repositories.model.SubscriptionInfoDAO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubscriptionInfoMapper {

    @Mapping(source = "subscriptionId", target = "subscriptionId")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "subscriptionType", target = "subscriptionType")
    @Mapping(source = "cost", target = "cost")
    @Mapping(source = "paymentDate", target = "paymentDate")
    SubscriptionInfo subscriptionInfoDAOToSubscriptionInfo(SubscriptionInfoDAO subscriptionInfoDAO);

    @Mapping(source = "subscriptionId", target = "subscriptionId")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "subscriptionType", target = "subscriptionType")
    @Mapping(source = "cost", target = "cost")
    @Mapping(source = "paymentDate", target = "paymentDate")
    SubscriptionInfoDAO subscriptionInfoToSubscriptionInfoDAO(SubscriptionInfo subscriptionInfo);

}
