package com.example.JWT.Entity;

import com.example.JWT.Entity.UserInfo;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordReset {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
   private String otp;
   private Instant expiryTime;

    @OneToOne
    @JoinColumn(name="user_id" , referencedColumnName = "id")
    private UserInfo userInfo;
}
