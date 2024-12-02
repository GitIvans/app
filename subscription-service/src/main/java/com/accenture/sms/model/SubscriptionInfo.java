package com.accenture.sms.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@ApiModel(description = "Model of subscription information")
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor

public class SubscriptionInfo {

        @ApiModelProperty(notes = "The database generated subscription ID")
        private Long subscriptionId;

        @ApiModelProperty(notes = "User ID related to the subscription")
        @NotNull(message = "User ID must not be null")
        private Long userId;

        @ApiModelProperty(notes = "Type of the subscription")
        @NotNull(message = "Subscription type must not be null")
        private String subscriptionType;

        @ApiModelProperty(notes = "Cost of the subscription")
        @NotNull(message = "Cost must not be null")
        private Double cost;

        @ApiModelProperty(notes = "Payment date for the subscription")
        @NotNull(message = "Payment date must not be null")
        private LocalDate paymentDate;


}
