package com.example.JWT.Repository;

import com.example.JWT.Entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepo extends JpaRepository<UserInfo,Integer> {
    Optional<UserInfo> findByName(String username);
    Optional<UserInfo> findByEmail(String email);
}
