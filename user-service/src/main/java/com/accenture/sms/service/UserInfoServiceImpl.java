package com.accenture.sms.service;

import com.accenture.sms.dto.UserInfoDTO;
import com.accenture.sms.handlers.BadRequestException;
import com.accenture.sms.mapper.UserInfoMapper;
import com.accenture.sms.model.UserInfo;
import com.accenture.sms.repositories.UserInfoRepository;
import com.accenture.sms.repositories.model.UserInfoDAO;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class UserInfoServiceImpl implements UserInfoService {

    private final UserInfoRepository userInfoRepository;
    private final UserInfoMapper userInfoMapper;

    @Autowired
    public UserInfoServiceImpl(UserInfoRepository userInfoRepository, UserInfoMapper userInfoMapper) {
        this.userInfoRepository = userInfoRepository;
        this.userInfoMapper = userInfoMapper;
    }

    @Override
    public UserInfo registerUser(UserInfo userInfo) {
        log.debug("Registering new user: {}", userInfo);
        if (userInfoRepository.existsByEmail(userInfo.getEmail())) {
            throw new BadRequestException("User with email " + userInfo.getEmail() + " already exists");
        }
        try {
            UserInfoDAO userInfoDAO = userInfoMapper.userInfoToUserInfoDAO(userInfo);
            UserInfoDAO savedUserInfoDAO = userInfoRepository.save(userInfoDAO);
            return userInfoMapper.userInfoDAOToUserInfo(savedUserInfoDAO);
        } catch (DataIntegrityViolationException e) {
            log.error("Error registering user: {}", userInfo, e);
            throw new BadRequestException("User data is invalid or violates constraints");
        } catch (Exception e) {
            log.error("Unexpected error while registering user: {}", userInfo, e);
            throw new BadRequestException("Unexpected error during user registration");
        }
    }

    @Override
    public UserInfo getUserById(Long id) {
        log.debug("Fetching User Info with ID: {}", id);
        UserInfoDAO userInfoDAO = getUserInfoDAOById(id);
        UserInfo userInfo = userInfoMapper.userInfoDAOToUserInfo(userInfoDAO);
        log.info("Fetched User Info: {}", userInfo);
        return userInfo;
    }

    @Override
    public List<UserInfo> getAllUsers() {
        List<UserInfo> userInfoList = userInfoRepository.findAll().stream()
                .map(userInfoMapper::userInfoDAOToUserInfo)
                .collect(Collectors.toList());
        log.info("Fetched {} User Info records.", userInfoList.size());
        return userInfoList;
    }

    @Override
    public UserInfo updateUser(Long id, UserInfo updatedUserInfo) {
        log.debug("Updating User Info with ID: {}", id);
        updatedUserInfo.setId(id);
        UserInfo userInfo = saveUserInfo(updatedUserInfo, "updating");
        log.info("Updated User Info: {}", userInfo);
        return userInfo;
    }

    @Override
    public void deleteUser(Long id) {
        log.debug("Deleting User Info with ID: {}", id);
        UserInfoDAO userInfoDAO = getUserInfoDAOById(id);
        userInfoRepository.delete(userInfoDAO);
        log.info("Deleted User Info with ID: {}", id);
    }

    private UserInfo saveUserInfo(UserInfo userInfo, String action) {
        try {
            UserInfoDAO userInfoDAO = userInfoMapper.userInfoToUserInfoDAO(userInfo);
            UserInfoDAO savedUserInfoDAO = userInfoRepository.save(userInfoDAO);
            return userInfoMapper.userInfoDAOToUserInfo(savedUserInfoDAO);
        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation while {} User Info: {}", action, userInfo, e);
            throw new BadRequestException("Invalid data or constraint violation");
        } catch (Exception e) {
            log.error("Unexpected error while {} User Info: {}", action, userInfo, e);
            throw new BadRequestException("Unexpected error while " + action + " User Info");
        }
    }

    @Override
    public UserInfo getUserByUsername(String username) {
        log.debug("Fetching User Info with username: {}", username);
        UserInfoDAO userInfoDAO = userInfoRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("User not found with username: " + username));
        UserInfo userInfo = userInfoMapper.userInfoDAOToUserInfo(userInfoDAO);
        log.info("Fetched User Info: {}", userInfo);
        return userInfo;
    }

    private UserInfoDTO convertToDTO(UserInfoDAO userInfoDAO) {
        UserInfoDTO dto = new UserInfoDTO();
        dto.setUsername(userInfoDAO.getUsername());
        dto.setEmail(userInfoDAO.getEmail());
        dto.setPassword(userInfoDAO.getPassword());

        return dto;
    }

    private UserInfoDAO getUserInfoDAOById(Long id) {
        return userInfoRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("User Info not found with ID: " + id));
    }
}