package com.accenture.sms.mapper;

import com.accenture.sms.model.UserInfo;
import com.accenture.sms.repositories.model.UserInfoDAO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserInfoMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    UserInfo userInfoDAOToUserInfo(UserInfoDAO userInfoDAO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    UserInfoDAO userInfoToUserInfoDAO(UserInfo userInfo);
}
