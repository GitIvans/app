package com.accenture.sms.repository;

import com.accenture.sms.repository.model.NotificationDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<NotificationDAO, Long> {
    Optional<NotificationDAO> findByUserId(Long userId);

    List<NotificationDAO> findBySentFalseAndDueDate(LocalDate date);
}
