package com.accenture.sms.controller;

import com.accenture.sms.client.UserServiceClient;
import com.accenture.sms.dto.UserInfoDTO;
import com.accenture.sms.repository.model.NotificationDAO;
import com.accenture.sms.service.EmailService;
import com.accenture.sms.service.NotificationService;
import com.accenture.sms.swagger.HTMLResponseMessages;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Api(tags = "NOTIFICATION_CONTROLLER")
@Log4j2
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserServiceClient userServiceClient;
    private final EmailService emailService;

    @Autowired
    public NotificationController(NotificationService notificationService,
                                  UserServiceClient userServiceClient,
                                  EmailService emailService) {
        this.notificationService = notificationService;
        this.userServiceClient = userServiceClient;
        this.emailService = emailService;
    }

    @ApiOperation(value = "Send email notification to the user",
            notes = "Retrieves user information and sends an email notification.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    @GetMapping("/send-email/{userId}")
    public ResponseEntity<String> sendEmailToUser(
            @ApiParam(value = "User ID for email notification", required = true)
            @PathVariable Long userId) {
        log.info("Attempting to send email to user with ID: {}", userId);

        UserInfoDTO userInfo = userServiceClient.getUserById(userId);
        if (userInfo != null) {
            String email = userInfo.getEmail();
            String subject = "Subscription Notification";
            String message = "Hello " + userInfo.getFirstName() + ",\nYour subscription will renew soon.";

            emailService.sendNotification(email, subject, message);
            log.info("Email successfully sent to: {}", email);
            return ResponseEntity.ok("Email sent to: " + email);
        } else {
            log.warn("User with ID {} not found", userId);
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation(value = "Save notification settings",
            notes = "Saves notification preferences for the user.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })

    @PostMapping("/settings")
    public ResponseEntity<NotificationDAO> saveNotificationSettings(
            @ApiParam(value = "Notification settings for the user", required = true)
            @RequestBody NotificationDAO notificationDAO) {
        log.info("Saving notification settings for user: {}", notificationDAO.getUserId());

        NotificationDAO savedNotification = notificationService.saveNotificationSettings(notificationDAO);
        log.info("Notification settings saved successfully for user: {}", savedNotification.getUserId());
        return ResponseEntity.ok(savedNotification);
    }

    @ApiOperation(value = "Get notification settings by User ID",
            notes = "Retrieves notification settings for a specific user.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    @GetMapping("/settings/{userId}")
    public ResponseEntity<NotificationDAO> getNotificationSettings(
            @ApiParam(value = "User ID for retrieving notification settings", required = true)
            @PathVariable Long userId) {
        log.info("Retrieving notification settings for user with ID: {}", userId);

        Optional<NotificationDAO> notificationSettings = notificationService.getNotificationSettings(userId);
        return notificationSettings.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Notification settings not found for user with ID: {}", userId);
                    return ResponseEntity.notFound().build();
                });
    }
}

