package com.accenture.sms.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@ApiModel(description = "Model of subscription analytics information")
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionAnalytics {

    private Long id;

    @ApiModelProperty(notes = "User ID related to the subscription")
    @NotNull(message = "User ID must not be null")
    private Long userId;

    @ApiModelProperty(notes = "Total cost of subscriptions")
    private Double totalCost;
}
