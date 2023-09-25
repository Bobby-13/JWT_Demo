package com.example.JWT.Repository;

import com.example.JWT.Entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken,Integer> {
    Optional<RefreshToken> findByToken(String token);
}
