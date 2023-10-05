package com.example.JWT.Service;

import com.example.JWT.Entity.RefreshToken;
import com.example.JWT.Repository.RefreshTokenRepo;
import com.example.JWT.Repository.UserInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepo refreshTokenRepo;

    @Autowired
    private UserInfoRepo userInfoRepo;

    public RefreshToken createRefreshToken(String username)
    {
        RefreshToken refreshToken = RefreshToken.builder()
                .userInfo(userInfoRepo.findByEmail(username).get())
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(600000))//10
                .build();
        return refreshTokenRepo.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token)
    {
        return refreshTokenRepo.findByToken(token);
    }


    public RefreshToken verifyExpiration (RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now()) < 0){
            refreshTokenRepo.delete(token);
            throw new RuntimeException(token.getToken()+"Refresh Token was Expired.Please make a new SignIn");
        }
        return token;
    }

}


