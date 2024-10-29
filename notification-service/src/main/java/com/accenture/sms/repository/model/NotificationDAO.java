package com.accenture.sms.repository.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "notification_period", nullable = false)
    private Integer notificationPeriod;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "sent", nullable = false)
    private Boolean sent = false;
}
