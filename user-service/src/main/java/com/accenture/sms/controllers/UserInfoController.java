package com.accenture.sms.controllers;

import com.accenture.sms.model.UserInfo;
import com.accenture.sms.repositories.UserInfoRepository;
import com.accenture.sms.service.UserInfoService;
import com.accenture.sms.swagger.DescriptionVariables;
import com.accenture.sms.swagger.HTMLResponseMessages;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = DescriptionVariables.USER_INFO_CONTROLLER)
@Log4j2
@RestController
@RequestMapping("api/user/info")
public class UserInfoController {

    private final UserInfoService userInfoService;
    private final UserInfoRepository userInfoRepository;


    @Autowired
    public UserInfoController(UserInfoService userInfoService, UserInfoRepository userInfoRepository) {
        this.userInfoService = userInfoService;
        this.userInfoRepository = userInfoRepository;
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkUsernameAndEmail(@RequestParam String username, @RequestParam String email) {
        boolean exists = userInfoRepository.existsByUsernameOrEmail(username, email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping
    @ApiOperation(value = "Finds all Users",
            notes = "Returns a list of Users",
            response = UserInfo.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200, response = UserInfo.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public ResponseEntity<List<UserInfo>> findAllUsers() {
        log.info("Retrieving list of Users");
        List<UserInfo> userInfoList = userInfoService.getAllUsers();

        if (userInfoList.isEmpty()) {
            log.warn("User list is empty!");
            return ResponseEntity.ok(userInfoList);
        }

        log.debug("Found User list. Size: {}", userInfoList.size());
        return ResponseEntity.ok(userInfoList);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Finds User by ID",
            notes = "Provide an ID to look up a specific User in the database",
            response = UserInfo.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public ResponseEntity<?> getUserById(@ApiParam(value = "ID of User", required = true)
                                         @NonNull @PathVariable Long id) {
        log.info("Finding User by ID: {}", id);

        UserInfo userInfo = userInfoService.getUserById(id);
        if (userInfo != null) {
            return ResponseEntity.ok(userInfo);
        } else {
            log.warn("User with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @ApiOperation(value = "Registers User", notes = "Registers a new User if valid",
            response = UserInfo.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = HTMLResponseMessages.HTTP_201),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserInfo userInfo,
                                          BindingResult bindingResult) {
        log.info("Creating a new User: {}", userInfo);

        ResponseEntity<?> validationResponse = validateBindingResult(bindingResult);
        if (validationResponse != null) {
            return validationResponse;
        }

        UserInfo savedUser = userInfoService.registerUser(userInfo);
        log.debug("New User is created: {}", savedUser);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Updates User by ID", notes = "Updates an existing User if the provided ID exists",
            response = UserInfo.class)
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Accepted"),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> updateUserById(@ApiParam(value = "ID of User", required = true)
                                            @NonNull @PathVariable Long id,
                                            @Valid @RequestBody UserInfo userInfoUpdate,
                                            BindingResult bindingResult) {
        log.info("Attempting to update User with ID: {}", id);

        if (userInfoUpdate.getId() == null) {
            return ResponseEntity.badRequest().body("ID in object must not be null when updating User.");
        }

        if (!userInfoUpdate.getId().equals(id)) {
            return ResponseEntity.badRequest().body("ID in object must match the ID in the URL.");
        }

        ResponseEntity<?> validationResponse = validateBindingResult(bindingResult);
        if (validationResponse != null) {
            return validationResponse;
        }

        UserInfo updatedUser = userInfoService.updateUser(id, userInfoUpdate);
        log.info("User with ID: {} successfully updated", id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedUser);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete User by ID",
            notes = "Delete User if provided ID exists",
            response = UserInfo.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = HTMLResponseMessages.HTTP_204_WITHOUT_DATA),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public ResponseEntity<?> deleteUserById(@ApiParam(value = "ID of User", required = true)
                                            @NonNull @PathVariable Long id) {
        log.info("Deleting User with ID: {}", id);

        userInfoService.deleteUser(id);
        log.info("User with ID: {} successfully deleted", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/username/{username}")
    @ApiOperation(value = "Finds User by Username",
            notes = "Provide a username to look up a specific User in the database",
            response = UserInfo.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public ResponseEntity<?> getUserByUsername(@ApiParam(value = "Username of User", required = true)
                                               @NonNull @PathVariable String username) {
        log.info("Finding User by username: {}", username);

        UserInfo userInfo = userInfoService.getUserByUsername(username);
        return ResponseEntity.ok(userInfo);
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
