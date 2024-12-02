package com.accenture.sms.tests.controllers;

import com.accenture.sms.controllers.UserInfoController;
import com.accenture.sms.model.UserInfo;
import com.accenture.sms.repositories.UserInfoRepository;
import com.accenture.sms.service.UserInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class UserInfoControllerTest {

    @Mock
    private UserInfoService userInfoService;

    @Mock
    private UserInfoRepository userInfoRepository;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private UserInfoController userInfoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCheckUsernameAndEmail() {
        String username = "testuser";
        String email = "test@test.com";

        when(userInfoRepository.existsByUsernameOrEmail(username, email)).thenReturn(true);

        ResponseEntity<Boolean> response = userInfoController.checkUsernameAndEmail(username, email);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());

        verify(userInfoRepository, times(1)).existsByUsernameOrEmail(username, email);
    }

    @Test
    void testFindAllUsers() {
        List<UserInfo> users = Collections.singletonList(new UserInfo(1L, "testuser", "password", "test@test.com", "John", "Doe"));

        when(userInfoService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<UserInfo>> response = userInfoController.findAllUsers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(userInfoService, times(1)).getAllUsers();
    }

    @Test
    void testGetUserById_Success() {
        UserInfo userInfo = new UserInfo(1L, "testuser", "password", "test@test.com", "John", "Doe");

        when(userInfoService.getUserById(1L)).thenReturn(userInfo);

        ResponseEntity<?> response = userInfoController.getUserById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userInfo, response.getBody());
        verify(userInfoService, times(1)).getUserById(1L);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userInfoService.getUserById(anyLong())).thenReturn(null);

        ResponseEntity<?> response = userInfoController.getUserById(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userInfoService, times(1)).getUserById(1L);
    }

    @Test
    void testRegisterUser_Success() {
        UserInfo userInfo = new UserInfo(1L, "testuser", "password", "test@test.com", "John", "Doe");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(userInfoService.registerUser(any(UserInfo.class))).thenReturn(userInfo);

        ResponseEntity<?> response = userInfoController.registerUser(userInfo, bindingResult);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userInfo, response.getBody());

        verify(userInfoService, times(1)).registerUser(userInfo);
    }

    @Test
    void testRegisterUser_ValidationError() {
        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<?> response = userInfoController.registerUser(new UserInfo(), bindingResult);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testUpdateUser_Success() {
        UserInfo updatedUserInfo = new UserInfo(1L, "updatedUser", "password", "updated@test.com", "Jane", "Smith");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(userInfoService.updateUser(anyLong(), any(UserInfo.class))).thenReturn(updatedUserInfo);

        ResponseEntity<?> response = userInfoController.updateUserById(1L, updatedUserInfo, bindingResult);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(updatedUserInfo, response.getBody());

        verify(userInfoService, times(1)).updateUser(1L, updatedUserInfo);
    }

    @Test
    void testDeleteUser_Success() {
        doNothing().when(userInfoService).deleteUser(anyLong());

        ResponseEntity<?> response = userInfoController.deleteUserById(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(userInfoService, times(1)).deleteUser(1L);
    }

    @Test
    void testGetUserByUsername() {
        String username = "testuser";
        UserInfo userInfo = new UserInfo(1L, "testuser", "password", "test@test.com", "John", "Doe");

        when(userInfoService.getUserByUsername(username)).thenReturn(userInfo);

        ResponseEntity<?> response = userInfoController.getUserByUsername(username);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userInfo, response.getBody());
        verify(userInfoService, times(1)).getUserByUsername(username);
    }
}
