package com.accenture.sms.repositories;

import com.accenture.sms.repositories.model.UserInfoDAO;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfoDAO, Long> {

    Optional<UserInfoDAO> findByUsername(String username);

    boolean existsByUsernameOrEmail(String username, String email);

    boolean existsByEmail(String email);

}
