package com.accenture.sms.controllers;

import com.accenture.sms.dto.SubscriptionAnalysisResultDTO;
import com.accenture.sms.service.SubscriptionAnalyticsServiceImpl;
import com.accenture.sms.swagger.DescriptionVariables;
import com.accenture.sms.swagger.HTMLResponseMessages;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = DescriptionVariables.SUBSCRIPTION_ANALYTICS_CONTROLLER)
@Log4j2
@RestController
@RequestMapping("/api/subscription/analytics")
public class SubscriptionAnalyticsController {

    private final SubscriptionAnalyticsServiceImpl analyticsService;

    @Autowired
    public SubscriptionAnalyticsController(SubscriptionAnalyticsServiceImpl analyticsService) {
        this.analyticsService = analyticsService;
    }

    @ApiOperation(value = "Analyze subscriptions for a user",
            notes = "Analyzes subscription data for a given user by their ID",
            response = SubscriptionAnalysisResultDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200, response = SubscriptionAnalysisResultDTO.class),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    @GetMapping("/user/{userId}/analysis")
    public ResponseEntity<?> analyzeUserSubscriptions(@ApiParam(value = "ID of User", required = true)
                                                      @NonNull @PathVariable Long userId) {
        log.info("Analyzing subscriptions for User ID: {}", userId);

        try {
            SubscriptionAnalysisResultDTO analysisResult = analyticsService.analyzeUserSubscriptions(userId);
            log.debug("Analysis completed for User ID: {}", userId);
            return ResponseEntity.ok(analysisResult);
        } catch (IllegalArgumentException e) {
            log.error("Invalid request: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Error occurred while analyzing subscriptions for User ID: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

//    @ApiOperation(value = "Get all subscription analytics",
//            notes = "Returns a list of all subscription analytics",
//            response = SubscriptionAnalysisResultDTO.class, responseContainer = "List")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200, response = SubscriptionAnalysisResultDTO.class, responseContainer = "List"),
//            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
//    })
//    @GetMapping
//    public ResponseEntity<List<SubscriptionAnalysisResultDTO>> getAllSubscriptionAnalytics() {
//        log.info("Retrieving all subscription analytics");
//
//        List<SubscriptionAnalysisResultDTO> analyticsList = analyticsService.getAllSubscriptionAnalytics();
//
//        if (analyticsList.isEmpty()) {
//            log.warn("No analytics data found.");
//        } else {
//            log.debug("Found {} analytics records", analyticsList.size());
//        }
//
//        return ResponseEntity.ok(analyticsList);
//    }
//
//    @ApiOperation(value = "Analyze subscriptions by subscription type",
//            notes = "Analyzes subscription data by subscription type",
//            response = SubscriptionAnalysisResultDTO.class)
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
//            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
//            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
//    })
//    @GetMapping("/type/{type}")
//    public ResponseEntity<?> analyzeBySubscriptionType(@ApiParam(value = "Subscription type", required = true)
//                                                       @NonNull @PathVariable String type) {
//        log.info("Analyzing subscriptions by type: {}", type);
//
//        try {
//            List<SubscriptionAnalysisResultDTO> analysisResults = analyticsService.analyzeBySubscriptionType(type);
//            log.debug("Analysis by type completed: {}", type);
//            return ResponseEntity.ok(analysisResults);
//        } catch (IllegalArgumentException e) {
//            log.error("Invalid subscription type: {}", e.getMessage());
//            return ResponseEntity.badRequest().body(e.getMessage());
//        } catch (Exception e) {
//            log.error("Error occurred while analyzing subscriptions by type: {}", type, e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
//        }
//    }
}
