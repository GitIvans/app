package com.accenture.sms.repositories.model;

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
@Table(name = "subscriptions_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionInfoDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long subscriptionId;

    @Column(name="user_id", nullable = false)
    private Long userId;

    @Column(name="subscription_type", nullable = false)
    private String subscriptionType;

    @Column(name = "cost", nullable = false)
    private Double cost;

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;
}
