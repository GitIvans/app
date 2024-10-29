package com.accenture.sms.scheduler;

import com.accenture.sms.client.UserServiceClient;
import com.accenture.sms.dto.UserInfoDTO;
import com.accenture.sms.repository.model.NotificationDAO;
import com.accenture.sms.service.EmailService;
import com.accenture.sms.service.NotificationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class NotificationScheduler {

    private final NotificationService notificationService;
    private final UserServiceClient userServiceClient;
    private final EmailService emailService;

    @Autowired
    public NotificationScheduler(NotificationService notificationService, UserServiceClient userServiceClient, EmailService emailService) {
        this.notificationService = notificationService;
        this.userServiceClient = userServiceClient;
        this.emailService = emailService;
    }

    @Scheduled(cron = "*/30 * * * * *") // Каждые 30 секунд
    public void sendDailyNotifications() {
        log.info("Starting daily notifications dispatch");

        List<NotificationDAO> notificationsToSend = notificationService.getNotificationsToSendToday();

        for (NotificationDAO notification : notificationsToSend) {
            Long userId = notification.getUserId();
            String message = notification.getMessage();

            UserInfoDTO userInfo = userServiceClient.getUserById(userId);

            if (userInfo != null) {
                String email = userInfo.getEmail();
                String subject = "Notification from Notification Service";

                emailService.sendNotification(email, subject, message);

                notificationService.markAsSent(notification.getId());
                log.info("Notification sent to user: {}", userId);
            } else {
                log.warn("User with ID {} not found. Skipping notification.", userId);
            }

        }
    }
}
