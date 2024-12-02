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
@Table(name = "subscription_analytics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionAnalyticsDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "total_cost")
    private Double totalCost;

    // new fields
    @Column(name = "total_subscriptions")
    private Integer totalSubscriptions;

    @Column(name = "active_subscriptions")
    private Integer activeSubscriptions;

    @Column(name = "average_cost")
    private Double averageCost;


}
