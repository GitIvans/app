package com.accenture.sms.controllers;

import com.accenture.sms.model.SubscriptionInfo;
import com.accenture.sms.service.SubscriptionInfoService;
import com.accenture.sms.swagger.DescriptionVariables;
import com.accenture.sms.swagger.HTMLResponseMessages;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiParam;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = DescriptionVariables.SUBSCRIPTION_INFO_CONTROLLER)
@Log4j2
@RestController
@RequestMapping("/api/subscription")
public class SubscriptionInfoController {

    private final SubscriptionInfoService subscriptionInfoService;

    @Autowired
    public SubscriptionInfoController(SubscriptionInfoService subscriptionInfoService) {
        this.subscriptionInfoService = subscriptionInfoService;
    }

    @GetMapping
    @ApiOperation(value = "Finds all Subscription Info",
            notes = "Returns a list of Subscription Info",
            response = SubscriptionInfo.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200, response = SubscriptionInfo.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public ResponseEntity<List<SubscriptionInfo>> findAllSubscriptions() {
        log.info("Retrieving list of Subscription Info");
        List<SubscriptionInfo> subscriptionInfoList = subscriptionInfoService.getAllSubscriptions();

        if (subscriptionInfoList.isEmpty()) {
            log.warn("Subscription Info list is empty!");
            return ResponseEntity.ok(subscriptionInfoList);
        }

        log.debug("Found Subscription Info list. Size: {}", subscriptionInfoList.size());
        return ResponseEntity.ok(subscriptionInfoList);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Finds Subscription Info by ID",
            notes = "Provide an ID to look up a specific Subscription Info in the database",
            response = SubscriptionInfo.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public ResponseEntity<?> getSubscriptionInfoById(@ApiParam(value = "ID of Subscription Info", required = true)
                                                     @NonNull @PathVariable Long id) {
        log.info("Finding Subscription Info by ID: {}", id);

        SubscriptionInfo subscriptionInfo = subscriptionInfoService.getSubscriptionInfoById(id);
        if (subscriptionInfo != null) {
            return ResponseEntity.ok(subscriptionInfo);
        } else {
            log.warn("Subscription Info with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @ApiOperation(value = "Saves Subscription Info", notes = "Saves Subscription Info if it is valid",
            response = SubscriptionInfo.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = HTMLResponseMessages.HTTP_201),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> saveSubscriptionInfo(@Valid @RequestBody SubscriptionInfo subscriptionInfo,
                                                  BindingResult bindingResult) {
        log.info("Creating a new Subscription Info: {}", subscriptionInfo);

        if (subscriptionInfo.getSubscriptionId() != null) {
            return ResponseEntity.badRequest().body("Subscription ID must be null.");
        }

        ResponseEntity<?> validationResponse = validateBindingResult(bindingResult);
        if (validationResponse != null) {
            return validationResponse;
        }

        SubscriptionInfo savedSubscriptionInfo = subscriptionInfoService.saveSubscriptionInfo(subscriptionInfo);
        log.debug("New Subscription Info created: {}", savedSubscriptionInfo);
        return new ResponseEntity<>(savedSubscriptionInfo, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Updates Subscription Info by ID", notes = "Updates an existing Subscription Info if the provided ID exists",
            response = SubscriptionInfo.class)
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Accepted"),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> updateSubscriptionInfoById(@ApiParam(value = "ID of Subscription Info", required = true)
                                                        @NonNull @PathVariable Long id,
                                                        @Valid @RequestBody SubscriptionInfo subscriptionInfoUpdate,
                                                        BindingResult bindingResult) {
        log.info("Attempting to update Subscription Info with ID: {}", id);

        if (subscriptionInfoUpdate.getSubscriptionId() == null) {
            return ResponseEntity.badRequest().body("Subscription ID in object must not be null when updating.");
        }

        if (!subscriptionInfoUpdate.getSubscriptionId().equals(id)) {
            return ResponseEntity.badRequest().body("Subscription ID in object must match the ID in the URL.");
        }

        ResponseEntity<?> validationResponse = validateBindingResult(bindingResult);
        if (validationResponse != null) {
            return validationResponse;
        }

        SubscriptionInfo updatedSubscriptionInfo = subscriptionInfoService.updateSubscriptionInfo(id, subscriptionInfoUpdate);
        log.info("Subscription Info with ID: {} successfully updated", id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedSubscriptionInfo);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete Subscription Info by ID",
            notes = "Delete Subscription Info if provided ID exists",
            response = SubscriptionInfo.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = HTMLResponseMessages.HTTP_204_WITHOUT_DATA),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public ResponseEntity<?> deleteSubscriptionInfoById(@ApiParam(value = "ID of Subscription Info", required = true)
                                                        @NonNull @PathVariable Long id) {
        log.info("Deleting Subscription Info with ID: {}", id);

        subscriptionInfoService.deleteSubscriptionInfo(id);
        log.info("Subscription Info with ID: {} successfully deleted", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    @ApiOperation(value = "Fetch subscriptions by User ID", notes = "Retrieves all subscriptions associated with a specific User ID",
            response = SubscriptionInfo.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    public ResponseEntity<List<SubscriptionInfo>> getSubscriptionsByUserId(@PathVariable Long userId) {
        log.info("Received request to fetch subscriptions for User ID: {}", userId);

        List<SubscriptionInfo> subscriptions = subscriptionInfoService.getSubscriptionsByUserId(userId);

        if (subscriptions.isEmpty()) {
            log.warn("No subscriptions found for User ID: {}", userId);
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(subscriptions);
    }

    private ResponseEntity<?> validateBindingResult(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("Validation errors: {}", bindingResult);
            List<String> errorMessages = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());

            return ResponseEntity.badRequest().body(errorMessages);
        }
        return null;
    }
}
