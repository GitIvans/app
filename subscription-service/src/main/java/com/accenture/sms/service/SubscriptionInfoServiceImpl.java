package com.accenture.sms.service;

import com.accenture.sms.handlers.BadRequestException;
import com.accenture.sms.mapper.SubscriptionInfoMapper;
import com.accenture.sms.model.SubscriptionInfo;
import com.accenture.sms.repositories.SubscriptionInfoRepository;
import com.accenture.sms.repositories.model.SubscriptionInfoDAO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class SubscriptionInfoServiceImpl implements SubscriptionInfoService {

    private final SubscriptionInfoRepository subscriptionInfoRepository;
    private final SubscriptionInfoMapper subscriptionInfoMapper;

    @Autowired
    public SubscriptionInfoServiceImpl(SubscriptionInfoRepository subscriptionInfoRepository, SubscriptionInfoMapper subscriptionInfoMapper) {
        this.subscriptionInfoRepository = subscriptionInfoRepository;
        this.subscriptionInfoMapper = subscriptionInfoMapper;
    }

    @Override
    public SubscriptionInfo saveSubscriptionInfo(SubscriptionInfo subscriptionInfo) {
        log.debug("Adding new subscription: {}", subscriptionInfo);
        try {
            SubscriptionInfoDAO subscriptionInfoDAO = subscriptionInfoMapper.subscriptionInfoToSubscriptionInfoDAO(subscriptionInfo);
            SubscriptionInfoDAO savedSubscriptionInfoDAO = subscriptionInfoRepository.save(subscriptionInfoDAO);
            return subscriptionInfoMapper.subscriptionInfoDAOToSubscriptionInfo(savedSubscriptionInfoDAO);
        } catch (DataIntegrityViolationException e) {
            log.error("Error adding subscription: {}", subscriptionInfo, e);
            throw new BadRequestException("Subscription data is invalid or violates constraints");
        } catch (Exception e) {
            log.error("Unexpected error while adding subscription: {}", subscriptionInfo, e);
            throw new BadRequestException("Unexpected error during subscription addition");
        }
    }

    @Override
    public SubscriptionInfo updateSubscriptionInfo(Long subscriptionId, SubscriptionInfo subscriptionInfoUpdate) {
        log.debug("Updating Subscription Info with ID: {}", subscriptionId);
        subscriptionInfoUpdate.setSubscriptionId(subscriptionId);
        SubscriptionInfo subscriptionInfo = saveSubscriptionInfo(subscriptionInfoUpdate);
        log.info("Updated Subscription Info: {}", subscriptionInfo);
        return subscriptionInfo;
    }

    @Override
    public SubscriptionInfo getSubscriptionInfoById(Long subscriptionId) {
        log.debug("Fetching Subscription Info with ID: {}", subscriptionId);
        SubscriptionInfoDAO subscriptionInfoDAO = getSubscriptionInfoDAOById(subscriptionId);
        SubscriptionInfo subscriptionInfo = subscriptionInfoMapper.subscriptionInfoDAOToSubscriptionInfo(subscriptionInfoDAO);
        log.info("Fetched Subscription Info: {}", subscriptionInfo);
        return subscriptionInfo;
    }

    @Override
    public List<SubscriptionInfo> getAllSubscriptions() {
        List<SubscriptionInfo> subscriptionInfoList = subscriptionInfoRepository.findAll().stream()
                .map(subscriptionInfoMapper::subscriptionInfoDAOToSubscriptionInfo)
                .collect(Collectors.toList());
        log.info("Fetched {} Subscription Info records.", subscriptionInfoList.size());
        return subscriptionInfoList;
    }

    @Override
    public void deleteSubscriptionInfo(Long subscriptionId) {
        log.debug("Deleting Subscription Info with ID: {}", subscriptionId);
        SubscriptionInfoDAO subscriptionInfoDAO = getSubscriptionInfoDAOById(subscriptionId);
        subscriptionInfoRepository.delete(subscriptionInfoDAO);
        log.info("Deleted Subscription Info with ID: {}", subscriptionId);
    }

    @Override
    public List<SubscriptionInfo> getSubscriptionsByUserId(Long userId) {
        log.debug("Fetching subscriptions for User ID: {}", userId);
        List<SubscriptionInfo> subscriptionInfoList = subscriptionInfoRepository.findByUserId(userId).stream()
                .map(subscriptionInfoMapper::subscriptionInfoDAOToSubscriptionInfo)
                .collect(Collectors.toList());
        log.info("Fetched {} Subscription Info records for User ID: {}", subscriptionInfoList.size(), userId);
        return subscriptionInfoList;
    }

    private SubscriptionInfoDAO getSubscriptionInfoDAOById(Long subscriptionId) {
        return subscriptionInfoRepository.findById(subscriptionId)
                .orElseThrow(() -> new BadRequestException("Subscription Info not found with ID: " + subscriptionId));
    }
}
