package com.accenture.sms.service;

import com.accenture.sms.repository.model.NotificationDAO;
import com.accenture.sms.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public NotificationDAO saveNotificationSettings(NotificationDAO notificationDAO) {
        return notificationRepository.save(notificationDAO);
    }

    public Optional<NotificationDAO> getNotificationSettings(Long userId) {
        return notificationRepository.findByUserId(userId);
    }

    public List<NotificationDAO> getNotificationsToSendToday() {
        LocalDate today = LocalDate.now();
        return notificationRepository.findBySentFalseAndDueDate(today.plusDays(0));
    }

    public void markAsSent(Long notificationId) {
        Optional<NotificationDAO> notificationOpt = notificationRepository.findById(notificationId);
        notificationOpt.ifPresent(notification -> {
            notification.setSent(true);
            notificationRepository.save(notification);
        });
    }
}
