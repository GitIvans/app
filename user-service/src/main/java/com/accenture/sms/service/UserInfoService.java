package com.accenture.sms.service;

import com.accenture.sms.dto.UserInfoDTO;
import com.accenture.sms.model.UserInfo;
import com.accenture.sms.repositories.model.UserInfoDAO;

import java.util.List;
import java.util.Optional;

public interface UserInfoService {

    UserInfo registerUser(UserInfo userInfo);

    UserInfo getUserById(Long id);

    List<UserInfo> getAllUsers();

    void deleteUser(Long id);

    UserInfo updateUser(Long id, UserInfo userInfo);

    UserInfo getUserByUsername(String username);

}