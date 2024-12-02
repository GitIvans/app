package com.accenture.sms.tests.service;

import com.accenture.sms.dto.UserInfoDTO;
import com.accenture.sms.handlers.BadRequestException;
import com.accenture.sms.mapper.UserInfoMapper;
import com.accenture.sms.model.UserInfo;
import com.accenture.sms.repositories.UserInfoRepository;
import com.accenture.sms.repositories.model.UserInfoDAO;
import com.accenture.sms.service.UserInfoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserInfoServiceImplTest {

    @Mock
    private UserInfoRepository userInfoRepository;

    @Mock
    private UserInfoMapper userInfoMapper;

    @InjectMocks
    private UserInfoServiceImpl userInfoService;

    private UserInfo userInfo;
    private UserInfoDAO userInfoDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userInfo = new UserInfo(1L, "testuser", "password123", "test@example.com", "John", "Doe");
        userInfoDAO = new UserInfoDAO(1L, "testuser", "password123", "test@example.com", "John", "Doe");
    }

    @Test
    void registerUser_Success() {
        when(userInfoRepository.existsByEmail(userInfo.getEmail())).thenReturn(false);
        when(userInfoMapper.userInfoToUserInfoDAO(userInfo)).thenReturn(userInfoDAO);
        when(userInfoRepository.save(any(UserInfoDAO.class))).thenReturn(userInfoDAO);
        when(userInfoMapper.userInfoDAOToUserInfo(userInfoDAO)).thenReturn(userInfo);

        UserInfo result = userInfoService.registerUser(userInfo);

        assertNotNull(result);
        assertEquals(userInfo.getEmail(), result.getEmail());
        verify(userInfoRepository).existsByEmail(userInfo.getEmail());
        verify(userInfoRepository).save(any(UserInfoDAO.class));
    }

    @Test
    void registerUser_UserAlreadyExists() {
        when(userInfoRepository.existsByEmail(userInfo.getEmail())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> userInfoService.registerUser(userInfo));

        verify(userInfoRepository, never()).save(any(UserInfoDAO.class));
    }

    @Test
    void registerUser_DataIntegrityViolation() {
        when(userInfoRepository.existsByEmail(userInfo.getEmail())).thenReturn(false);
        when(userInfoMapper.userInfoToUserInfoDAO(userInfo)).thenReturn(userInfoDAO);
        when(userInfoRepository.save(any(UserInfoDAO.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(BadRequestException.class, () -> userInfoService.registerUser(userInfo));

        verify(userInfoRepository).save(any(UserInfoDAO.class));
    }

    @Test
    void getUserById_Success() {
        when(userInfoRepository.findById(userInfo.getId())).thenReturn(Optional.of(userInfoDAO));
        when(userInfoMapper.userInfoDAOToUserInfo(userInfoDAO)).thenReturn(userInfo);

        UserInfo result = userInfoService.getUserById(userInfo.getId());

        assertNotNull(result);
        assertEquals(userInfo.getId(), result.getId());
        verify(userInfoRepository).findById(userInfo.getId());
    }

    @Test
    void getUserById_NotFound() {
        when(userInfoRepository.findById(userInfo.getId())).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> userInfoService.getUserById(userInfo.getId()));

        verify(userInfoRepository).findById(userInfo.getId());
    }

    @Test
    void getAllUsers_Success() {
        List<UserInfoDAO> userInfoDAOs = new ArrayList<>();
        userInfoDAOs.add(userInfoDAO);
        when(userInfoRepository.findAll()).thenReturn(userInfoDAOs);
        when(userInfoMapper.userInfoDAOToUserInfo(userInfoDAO)).thenReturn(userInfo);

        List<UserInfo> result = userInfoService.getAllUsers();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(userInfoRepository).findAll();
    }

    @Test
    void updateUser_Success() {
        when(userInfoRepository.findById(userInfo.getId())).thenReturn(Optional.of(userInfoDAO));
        when(userInfoMapper.userInfoToUserInfoDAO(userInfo)).thenReturn(userInfoDAO);
        when(userInfoRepository.save(any(UserInfoDAO.class))).thenReturn(userInfoDAO);
        when(userInfoMapper.userInfoDAOToUserInfo(userInfoDAO)).thenReturn(userInfo);

        UserInfo result = userInfoService.updateUser(userInfo.getId(), userInfo);

        assertNotNull(result);
        assertEquals(userInfo.getId(), result.getId());
        verify(userInfoRepository).save(any(UserInfoDAO.class));
    }

    @Test
    void deleteUser_Success() {
        when(userInfoRepository.findById(userInfo.getId())).thenReturn(Optional.of(userInfoDAO));

        userInfoService.deleteUser(userInfo.getId());

        verify(userInfoRepository).delete(userInfoDAO);
    }

    @Test
    void getUserByUsername_Success() {
        when(userInfoRepository.findByUsername(userInfo.getUsername())).thenReturn(Optional.of(userInfoDAO));
        when(userInfoMapper.userInfoDAOToUserInfo(userInfoDAO)).thenReturn(userInfo);

        UserInfo result = userInfoService.getUserByUsername(userInfo.getUsername());

        assertNotNull(result);
        assertEquals(userInfo.getUsername(), result.getUsername());
        verify(userInfoRepository).findByUsername(userInfo.getUsername());
    }

    @Test
    void getUserByUsername_NotFound() {
        when(userInfoRepository.findByUsername(userInfo.getUsername())).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> userInfoService.getUserByUsername(userInfo.getUsername()));

        verify(userInfoRepository).findByUsername(userInfo.getUsername());
    }
}
