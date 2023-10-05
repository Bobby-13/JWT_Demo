package com.example.JWT.Repository;

import com.example.JWT.Entity.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetRepo extends JpaRepository<PasswordReset,Integer> {

    Optional<PasswordReset> findByOtp(String otp);
}
